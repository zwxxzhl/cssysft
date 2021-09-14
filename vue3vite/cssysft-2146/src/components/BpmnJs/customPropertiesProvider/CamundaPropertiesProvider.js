'use strict';

import inherits from 'inherits';

import PropertiesActivator from 'bpmn-js-properties-panel/lib/PropertiesActivator';

import asyncCapableHelper from 'bpmn-js-properties-panel/lib/helper/AsyncCapableHelper';
import ImplementationTypeHelper from 'bpmn-js-properties-panel/lib/helper/ImplementationTypeHelper';

import {
  is
} from 'bpmn-js/lib/util/ModelUtil';

// bpmn properties
import processProps from 'bpmn-js-properties-panel/lib/provider/bpmn/parts/ProcessProps';
import eventProps from 'bpmn-js-properties-panel/lib/provider/bpmn/parts/EventProps';
import linkProps from 'bpmn-js-properties-panel/lib/provider/bpmn/parts/LinkProps';
import documentationProps from 'bpmn-js-properties-panel/lib/provider/bpmn/parts/DocumentationProps';
import idProps from 'bpmn-js-properties-panel/lib/provider/bpmn/parts/IdProps';
import nameProps from 'bpmn-js-properties-panel/lib/provider/bpmn/parts/NameProps';
import executableProps from 'bpmn-js-properties-panel/lib/provider/bpmn/parts/ExecutableProps';


// camunda properties
import serviceTaskDelegateProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ServiceTaskDelegateProps';

/** custom 使用自定义 userTaskProps */
// import userTaskProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/UserTaskProps';
import userTaskProps from './parts/UserTaskProps';

import asynchronousContinuationProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/AsynchronousContinuationProps';
import callActivityProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/CallActivityProps';
import multiInstanceProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/MultiInstanceLoopProps';
import conditionalProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ConditionalProps';
import scriptProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ScriptTaskProps';
import errorProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ErrorEventProps';
import formProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/FormProps';
import startEventInitiator from 'bpmn-js-properties-panel/lib/provider/camunda/parts/StartEventInitiator';
import variableMapping from 'bpmn-js-properties-panel/lib/provider/camunda/parts/VariableMappingProps';
import versionTag from 'bpmn-js-properties-panel/lib/provider/camunda/parts/VersionTagProps';
import processVariablesProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ProcessVariablesProps';

import listenerProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ListenerProps';
import listenerDetails from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ListenerDetailProps';
import listenerFields from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ListenerFieldInjectionProps';


// element template properties
import elementTemplateDescriptionProps from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/parts/DescriptionProps';
import elementTemplateChooserProps from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/parts/ChooserProps';
import elementTemplateCustomProps from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/parts/CustomProps';
import elementTemplateInputParametersProps from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/parts/InputParametersProps';
import elementTemplateOutputParametersProps from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/parts/OutputParametersProps';
import elementTemplateErrorsProps from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/parts/ErrorsProps';

import {
  getTemplateId
} from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/Helper';


// Input/Output
import inputParameters from 'bpmn-js-properties-panel/lib/provider/camunda/parts/InputParametersProps';
import outputParameters from 'bpmn-js-properties-panel/lib/provider/camunda/parts/OutputParametersProps';
import errorsProps from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ErrorsProps';

// Connector
import connectorDetails from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ConnectorDetailProps';
import connectorInputParameters from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ConnectorInputParametersProps';
import connectorOutputParameters from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ConnectorOutputParametersProps';

// properties
import properties from 'bpmn-js-properties-panel/lib/provider/camunda/parts/PropertiesProps';

// job configuration
import jobConfiguration from 'bpmn-js-properties-panel/lib/provider/camunda/parts/JobConfigurationProps';

// history time to live
import historyTimeToLive from 'bpmn-js-properties-panel/lib/provider/camunda/parts/HistoryTimeToLiveProps';

// candidate starter groups/users
import candidateStarter from 'bpmn-js-properties-panel/lib/provider/camunda/parts/CandidateStarterProps';

// tasklist
import tasklist from 'bpmn-js-properties-panel/lib/provider/camunda/parts/TasklistProps';

// external task configuration
import externalTaskConfiguration from 'bpmn-js-properties-panel/lib/provider/camunda/parts/ExternalTaskConfigurationProps';

// field injection
import fieldInjections from 'bpmn-js-properties-panel/lib/provider/camunda/parts/FieldInjectionProps';

import {
  getBusinessObject
} from 'bpmn-js/lib/util/ModelUtil';

import eventDefinitionHelper from 'bpmn-js-properties-panel/lib/helper/EventDefinitionHelper';
import implementationTypeHelper from 'bpmn-js-properties-panel/lib/helper/ImplementationTypeHelper';

// helpers ////////////////////////////////////////

var isExternalTaskPriorityEnabled = function(element) {
  var businessObject = getBusinessObject(element);

  // show only if element is a process, a participant ...
  if (is(element, 'bpmn:Process') || is(element, 'bpmn:Participant') && businessObject.get('processRef')) {
    return true;
  }

  var externalBo = ImplementationTypeHelper.getServiceTaskLikeBusinessObject(element),
    isExternalTask = ImplementationTypeHelper.getImplementationType(externalBo) === 'external';

  // ... or an external task with selected external implementation type
  return !!ImplementationTypeHelper.isExternalCapable(externalBo) && isExternalTask;
};

var isJobConfigEnabled = function(element) {
  var businessObject = getBusinessObject(element);

  if (is(element, 'bpmn:Process') || is(element, 'bpmn:Participant') && businessObject.get('processRef')) {
    return true;
  }

  // async behavior
  var bo = getBusinessObject(element);
  if (asyncCapableHelper.isAsyncBefore(bo) || asyncCapableHelper.isAsyncAfter(bo)) {
    return true;
  }

  // timer definition
  if (is(element, 'bpmn:Event')) {
    return !!eventDefinitionHelper.getTimerEventDefinition(element);
  }

  return false;
};

var getListenerLabel = function(param, translate) {

  if (is(param, 'camunda:ExecutionListener')) {
    return translate('Execution Listener');
  }

  if (is(param, 'camunda:TaskListener')) {
    return translate('Task Listener');
  }

  return '';
};

var PROCESS_KEY_HINT = 'This maps to the process definition key.';
var TASK_KEY_HINT = 'This maps to the task definition key.';

function getIdOptions(element) {

  if (is(element, 'bpmn:Participant')) {
    return { id: 'participant-id', label: 'Participant Id' };
  }

  if (is(element, 'bpmn:Process')) {
    return { description: PROCESS_KEY_HINT };
  }

  if (is(element, 'bpmn:UserTask')) {
    return { description: TASK_KEY_HINT };
  }
}

function getNameOptions(element) {
  if (is(element, 'bpmn:Participant')) {
    return { id: 'participant-name', label: 'Participant Name' };
  }
}

function getProcessOptions(element) {
  if (is(element, 'bpmn:Participant')) {
    return { processIdDescription: PROCESS_KEY_HINT };
  }
}

function createElementTemplateGroups(
  element,
  bpmnFactory,
  canvas,
  commandStack,
  elementTemplates,
  modeling,
  replace,
  selection,
  translate) {
  var templateId = getTemplateId(element);

  if (!templateId) {
    return [];
  }

  var descriptionGroup = elementTemplateDescriptionProps(
    element, commandStack, elementTemplates, modeling, replace, selection, translate);

  var idOptions = getIdOptions(element) || {};

  idOptions.id = 'element-template-element-id';

  var nameOptions = { id: 'element-template-element-name' };

  idProps(descriptionGroup, element, translate, idOptions);
  nameProps(descriptionGroup, element, bpmnFactory, canvas, translate, nameOptions);
  processProps(descriptionGroup, element, translate, getProcessOptions(element));

  var elementTemplateInputParametersGroup = {
    id: 'template-inputs',
    label: translate('Input Parameters'),
    entries: []
  };
  elementTemplateInputParametersProps(elementTemplateInputParametersGroup, element, elementTemplates, bpmnFactory, translate);

  var elementTemplateOutputParametersGroup = {
    id: 'template-outputs',
    label: translate('Output Parameters'),
    entries: []
  };
  elementTemplateOutputParametersProps(elementTemplateOutputParametersGroup, element, elementTemplates, bpmnFactory, translate);


  var elementTemplateErrorsGroup = {
    id: 'template-errors',
    label: translate('Errors'),
    entries: []
  };
  elementTemplateErrorsProps(elementTemplateErrorsGroup, element, elementTemplates, bpmnFactory, translate);

  var customFieldsGroups = elementTemplateCustomProps(element, elementTemplates, bpmnFactory, translate);

  return [
    descriptionGroup,
    elementTemplateInputParametersGroup,
    elementTemplateOutputParametersGroup,
    elementTemplateErrorsGroup
  ].concat(customFieldsGroups);
}

function createGeneralTabGroups(
  element, canvas, bpmnFactory,
  elementRegistry, elementTemplates, translate) {

  // refer to target element for external labels
  element = element.labelTarget || element;

  var generalGroup = {
    id: 'general',
    label: translate('General'),
    entries: []
  };

  idProps(generalGroup, element, translate, getIdOptions(element));
  nameProps(generalGroup, element, bpmnFactory, canvas, translate, getNameOptions(element));
  processProps(generalGroup, element, translate, getProcessOptions(element));
  versionTag(generalGroup, element, translate);
  /** custom 流程主面板 可执行文件 隐藏默 因为认会勾选，不需要给到用户使用 */
  // executableProps(generalGroup, element, translate);
  elementTemplateChooserProps(generalGroup, element, elementTemplates, translate);

  var detailsGroup = {
    id: 'details',
    label: translate('Details'),
    entries: []
  };
  serviceTaskDelegateProps(detailsGroup, element, bpmnFactory, translate);
  userTaskProps(detailsGroup, element, translate);
  scriptProps(detailsGroup, element, bpmnFactory, translate);
  linkProps(detailsGroup, element, translate);
  callActivityProps(detailsGroup, element, bpmnFactory, translate);
  eventProps(detailsGroup, element, bpmnFactory, elementRegistry, translate);
  errorProps(detailsGroup, element, bpmnFactory, translate);
  /** custom 流程主面板以及各流程节点 条件类型 隐藏默 因为暂时不开发变量，以及网关使用 */
  // conditionalProps(detailsGroup, element, bpmnFactory, translate);
  startEventInitiator(detailsGroup, element, translate); // this must be the last element of the details group!

  var multiInstanceGroup = {
    id: 'multiInstance',
    label: translate('Multi Instance'),
    entries: []
  };
  multiInstanceProps(multiInstanceGroup, element, bpmnFactory, translate);

  var asyncGroup = {
    id : 'async',
    label: translate('Asynchronous Continuations'),
    entries : []
  };
  asynchronousContinuationProps(asyncGroup, element, bpmnFactory, translate);

  var jobConfigurationGroup = {
    id : 'jobConfiguration',
    label : translate('Job Configuration'),
    entries : [],
    enabled: isJobConfigEnabled
  };
  jobConfiguration(jobConfigurationGroup, element, bpmnFactory, translate);

  var externalTaskGroup = {
    id : 'externalTaskConfiguration',
    label : translate('External Task Configuration'),
    entries : [],
    enabled: isExternalTaskPriorityEnabled
  };
  externalTaskConfiguration(externalTaskGroup, element, bpmnFactory, translate);

  var candidateStarterGroup = {
    id: 'candidateStarterConfiguration',
    label: translate('Candidate Starter Configuration'),
    entries: []
  };
  candidateStarter(candidateStarterGroup, element, bpmnFactory, translate);

  var historyTimeToLiveGroup = {
    id: 'historyConfiguration',
    label: translate('History Configuration'),
    entries: []
  };
  historyTimeToLive(historyTimeToLiveGroup, element, bpmnFactory, translate);

  var tasklistGroup = {
    id: 'tasklist',
    label: translate('Tasklist Configuration'),
    entries: []
  };
  tasklist(tasklistGroup, element, bpmnFactory, translate);

  var documentationGroup = {
    id: 'documentation',
    label: translate('Documentation'),
    entries: []
  };
  documentationProps(documentationGroup, element, bpmnFactory, translate);

  var groups = [];
  groups.push(generalGroup);
  groups.push(detailsGroup);
  /** custom 主流程面板 扩展任务配置 隐藏 */
  // groups.push(externalTaskGroup);
  /** custom 主流程面板以及各流程节点 多重事件 隐藏 【右侧面板未看到有相关的输入框或文字】 */
  // groups.push(multiInstanceGroup);
  /** custom 主流程面板 持续异步 隐藏 */
  // groups.push(asyncGroup);
  /** custom 主流程面板 工作配置 隐藏 */
  // groups.push(jobConfigurationGroup);
  /** custom 主流程面板 候选人启动器配置 隐藏 */
  // groups.push(candidateStarterGroup);
  /** custom 主流程面板 历史配置 隐藏 */
  // groups.push(historyTimeToLiveGroup);
  /** custom 主流程面板 Tasklist Configuration 隐藏 */
  // groups.push(tasklistGroup);
  /** custom 主流程面板以及各流程节点 文档 隐藏 */
  groups.push(documentationGroup);

  return groups;
}

function createVariablesTabGroups(element, bpmnFactory, elementRegistry, translate) {
  var variablesGroup = {
    id : 'variables',
    label : translate('Variables'),
    entries: []
  };
  variableMapping(variablesGroup, element, bpmnFactory, translate);

  return [
    variablesGroup
  ];
}

function createProcessVariablesTabGroups(element, translate) {
  var processVariablesGroup = {
    id : 'process-variables',
    label : translate('Variables'),
    entries: []
  };

  processVariablesProps(processVariablesGroup, element, translate);

  return [
    processVariablesGroup
  ];
}

function createFormsTabGroups(element, bpmnFactory, elementRegistry, translate) {
  var formGroup = {
    id : 'forms',
    label : translate('Forms'),
    entries: []
  };
  formProps(formGroup, element, bpmnFactory, translate);

  return [
    formGroup
  ];
}

function createListenersTabGroups(element, bpmnFactory, elementRegistry, translate) {

  var listenersGroup = {
    id : 'listeners',
    label: translate('Listeners'),
    entries: []
  };

  var options = listenerProps(listenersGroup, element, bpmnFactory, translate);

  var listenerDetailsGroup = {
    id: 'listener-details',
    entries: [],
    enabled: function(element, node) {
      return options.getSelectedListener(element, node);
    },
    label: function(element, node) {
      var param = options.getSelectedListener(element, node);
      return getListenerLabel(param, translate);
    }
  };

  listenerDetails(listenerDetailsGroup, element, bpmnFactory, options, translate);

  var listenerFieldsGroup = {
    id: 'listener-fields',
    label: translate('Field Injection'),
    entries: [],
    enabled: function(element, node) {
      return options.getSelectedListener(element, node);
    }
  };

  listenerFields(listenerFieldsGroup, element, bpmnFactory, options, translate);

  return [
    listenersGroup,
    listenerDetailsGroup,
    listenerFieldsGroup
  ];
}

function createInputOutputTabGroups(element, bpmnFactory, elementTemplates, translate) {

  var inputParametersGroup = {
    id: 'input-parameters',
    label: translate('Input Parameters'),
    entries: []
  };

  inputParameters(inputParametersGroup, element, bpmnFactory, elementTemplates, translate);

  var outputParametersGroup = {
    id: 'output-parameters',
    label: translate('Output Parameters'),
    entries: []
  };

  outputParameters(outputParametersGroup, element, bpmnFactory, elementTemplates, translate);

  var errorsGroup = {
    id: 'errors',
    label: translate('Errors'),
    entries: [],

    enabled: function(element, node) {
      var businessObject = getBusinessObject(element);
      var isExternal = ImplementationTypeHelper.getImplementationType(businessObject) === 'external';

      return is(element, 'bpmn:ServiceTask') && isExternal;
    },
  };

  errorsProps(errorsGroup, element, bpmnFactory, elementTemplates, translate);

  return [
    inputParametersGroup,
    outputParametersGroup,
    errorsGroup
  ];
}

function createConnectorTabGroups(element, bpmnFactory, elementRegistry, translate) {
  var connectorDetailsGroup = {
    id: 'connector-details',
    label: translate('Details'),
    entries: []
  };

  connectorDetails(connectorDetailsGroup, element, bpmnFactory, translate);

  var connectorInputParametersGroup = {
    id: 'connector-input-parameters',
    label: translate('Input Parameters'),
    entries: []
  };

  connectorInputParameters(connectorInputParametersGroup, element, bpmnFactory, translate);

  var connectorOutputParametersGroup = {
    id: 'connector-output-parameters',
    label: translate('Output Parameters'),
    entries: []
  };

  connectorOutputParameters(connectorOutputParametersGroup, element, bpmnFactory, translate);

  return [
    connectorDetailsGroup,
    connectorInputParametersGroup,
    connectorOutputParametersGroup
  ];
}

function createFieldInjectionsTabGroups(element, bpmnFactory, elementRegistry, translate) {

  var fieldGroup = {
    id: 'field-injections-properties',
    label: translate('Field Injections'),
    entries: []
  };

  fieldInjections(fieldGroup, element, bpmnFactory, translate);

  return [
    fieldGroup
  ];
}

function createExtensionElementsGroups(element, bpmnFactory, elementRegistry, translate) {

  var propertiesGroup = {
    id : 'extensionElements-properties',
    label: translate('Properties'),
    entries: []
  };
  properties(propertiesGroup, element, bpmnFactory, translate);

  return [
    propertiesGroup
  ];
}

// Camunda Properties Provider /////////////////////////////////////


/**
 * A properties provider for Camunda related properties.
 *
 * @param {BpmnFactory} bpmnFactory
 * @param {Canvas} canvas
 * @param {ElementRegistry} elementRegistry
 * @param {ElementTemplates} elementTemplates
 * @param {EventBus} eventBus
 * @param {Modeling} modeling
 * @param {Replace} replace
 * @param {Selection} selection
 * @param {Translate} translate
 */
function CamundaPropertiesProvider(
  bpmnFactory,
  canvas,
  commandStack,
  elementRegistry,
  elementTemplates,
  eventBus,
  modeling,
  replace,
  selection,
  translate
) {
  PropertiesActivator.call(this, eventBus);

  this.getTabs = function(element) {

    var generalTab = {
      id: 'general',
      label: translate('General'),
      groups: createGeneralTabGroups(
        element, canvas, bpmnFactory,
        elementRegistry, elementTemplates, translate)
    };

    var elementTemplateTab = {
      id: 'element-template',
      label: translate('Template'),
      groups: createElementTemplateGroups(
        element,
        bpmnFactory,
        canvas,
        commandStack,
        elementTemplates,
        modeling,
        replace,
        selection,
        translate
      )
    };

    var variablesTab = {
      id: 'variables',
      label: translate('Variables'),
      groups: createVariablesTabGroups(element, bpmnFactory, elementRegistry, translate)
    };

    var processVariablesTab = {
      id: 'process-variables',
      label: translate('Variables'),
      groups: createProcessVariablesTabGroups(element, translate)
    };

    var formsTab = {
      id: 'forms',
      label: translate('Forms'),
      groups: createFormsTabGroups(element, bpmnFactory, elementRegistry, translate)
    };

    var listenersTab = {
      id: 'listeners',
      label: translate('Listeners'),
      groups: createListenersTabGroups(element, bpmnFactory, elementRegistry, translate),
      enabled: function(element) {
        return !eventDefinitionHelper.getLinkEventDefinition(element)
          || (!is(element, 'bpmn:IntermediateThrowEvent')
            && eventDefinitionHelper.getLinkEventDefinition(element));
      }
    };

    var inputOutputTab = {
      id: 'input-output',
      label: translate('Input/Output'),
      groups: createInputOutputTabGroups(element, bpmnFactory, elementTemplates, translate)
    };

    var connectorTab = {
      id: 'connector',
      label: translate('Connector'),
      groups: createConnectorTabGroups(element, bpmnFactory, elementRegistry, translate),
      enabled: function(element) {
        var bo = implementationTypeHelper.getServiceTaskLikeBusinessObject(element);
        return bo && implementationTypeHelper.getImplementationType(bo) === 'connector';
      }
    };

    var fieldInjectionsTab = {
      id: 'field-injections',
      label: translate('Field Injections'),
      groups: createFieldInjectionsTabGroups(element, bpmnFactory, elementRegistry, translate)
    };

    var extensionsTab = {
      id: 'extensionElements',
      label: translate('Extensions'),
      groups: createExtensionElementsGroups(element, bpmnFactory, elementRegistry, translate)
    };

    return [
      generalTab,
      // elementTemplateTab,
      /** custom 隐藏节点变量 */
      // variablesTab,
      /** custom 隐藏主流程变量 */
      // processVariablesTab,
      // connectorTab,
      /** custom 隐藏表单 */
      // formsTab,
      /** custom 隐藏监听器 */
      // listenersTab,
      /** custom 隐藏输入输出 */
      // inputOutputTab,
      // fieldInjectionsTab,
      /** custom 隐藏扩展 */
      // extensionsTab
    ];
  };

}

CamundaPropertiesProvider.$inject = [
  'bpmnFactory',
  'canvas',
  'commandStack',
  'elementRegistry',
  'elementTemplates',
  'eventBus',
  'modeling',
  'replace',
  'selection',
  'translate'
];

inherits(CamundaPropertiesProvider, PropertiesActivator);

export default CamundaPropertiesProvider;
