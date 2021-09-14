import {isAny} from "bpmn-js/lib/features/modeling/util/ModelingUtil";
import {assign, forEach} from "min-dash";
import {is} from "bpmn-js/lib/util/ModelUtil";
import {isEventSubProcess} from "bpmn-js/lib/util/DiUtil";

function CustomContextPadProvider(
  config,
  injector,
  contextPad,
  modeling,
  elementFactory,
  connect,
  create,
  translate) {

  config = config || {};

  contextPad.registerProvider(this);

  var autoPlace
  if (config.autoPlace !== false) {
    autoPlace = injector.get('autoPlace', false);
  }

  this.getContextPadEntries = function(element) {
    // no entries, effectively disable the context pad
    let actions = {};

    if (element.type === 'label') {
      return actions;
    }

    var businessObject = element.businessObject;

    if (!is(businessObject, 'bpmn:EndEvent') &&
      !businessObject.isForCompensation &&
      !isEventType(businessObject, 'bpmn:IntermediateThrowEvent', 'bpmn:LinkEventDefinition') &&
      !isEventSubProcess(businessObject)) {

      assign(actions, {
        'append.end-event': appendAction(
          'bpmn:EndEvent',
          'bpmn-icon-end-event-none',
          translate('Append EndEvent')
        ),
        'append.gateway': appendAction(
          'bpmn:ExclusiveGateway',
          'bpmn-icon-gateway-none',
          translate('Append Gateway')
        ),
        'append.append-task': appendAction(
          'bpmn:Task',
          'bpmn-icon-task',
          translate('Append Task')
        ),
        'append.intermediate-event': appendAction(
          'bpmn:IntermediateThrowEvent',
          'bpmn-icon-intermediate-event-none',
          translate('Append Intermediate/Boundary Event')
        )
      });
    }

    function startConnect(event, element) {
      connect.start(event, element);
    }

    if (
      isAny(businessObject, [
        'bpmn:FlowNode',
        'bpmn:InteractionNode',
        'bpmn:DataObjectReference',
        'bpmn:DataStoreReference',
      ])
    ) {
      assign(actions, {
        'append.text-annotation': appendAction(
          'bpmn:TextAnnotation',
          'bpmn-icon-text-annotation'
        ),

        'connect': {
          group: 'connect',
          className: 'bpmn-icon-connection-multi',
          title: translate(
            'Connect using ' +
            (businessObject.isForCompensation
              ? ''
              : 'Sequence/MessageFlow or ') +
            'Association'
          ),
          action: {
            click: startConnect,
            dragstart: startConnect,
          },
        },
      });
    }

    if (is(businessObject, 'bpmn:TextAnnotation')) {
      assign(actions, {
        'connect': {
          group: 'connect',
          className: 'bpmn-icon-connection-multi',
          title: translate('Connect using Association'),
          action: {
            click: startConnect,
            dragstart: startConnect,
          },
        },
      });
    }

    if (isAny(businessObject, [ 'bpmn:DataObjectReference', 'bpmn:DataStoreReference' ])) {
      assign(actions, {
        'connect': {
          group: 'connect',
          className: 'bpmn-icon-connection-multi',
          title: translate('Connect using DataInputAssociation'),
          action: {
            click: startConnect,
            dragstart: startConnect
          }
        }
      });
    }



    function removeElement(e) {
      modeling.removeElements([ element ]);
    }

    Object.assign(actions, {
      'delete': {
        group: 'edit',
        className: 'bpmn-icon-trash',
        title: translate('Remove'),
        action: {
          click: removeElement
        }
      }
    });

    function appendAction(type, className, title, options) {

      if (typeof title !== 'string') {
        options = title;
        title = translate('Append {type}', { type: type.replace(/^bpmn:/, '') });
      }

      function appendStart(event, element) {

        var shape = elementFactory.createShape(assign({ type: type }, options));
        create.start(event, shape, {
          source: element
        });
      }


      var append = autoPlace ? function(event, element) {
        var shape = elementFactory.createShape(assign({ type: type }, options));

        autoPlace.append(element, shape);
      } : appendStart;


      return {
        group: 'model',
        className: className,
        title: title,
        action: {
          dragstart: appendStart,
          click: append
        }
      };
    }

    function isEventType(eventBo, type, definition) {

      var isType = eventBo.$instanceOf(type);
      var isDefinition = false;

      var definitions = eventBo.eventDefinitions || [];
      forEach(definitions, function(def) {
        if (def.$type === definition) {
          isDefinition = true;
        }
      });

      return isType && isDefinition;
    }

    return actions;
  };
}
CustomContextPadProvider.$inject = [
  'config.contextPad',
  'injector',
  'contextPad',
  'modeling',
  'elementFactory',
  'connect',
  'create',
  'translate'
];

export default {
  contextPadProvider: ['type', CustomContextPadProvider]
};