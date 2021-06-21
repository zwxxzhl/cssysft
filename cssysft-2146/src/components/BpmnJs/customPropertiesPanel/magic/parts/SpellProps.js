import entryFactory from 'bpmn-js-properties-panel/lib/factory/EntryFactory';

import {
  getBusinessObject, getExtensionElements, is
} from 'bpmn-js/lib/util/ModelUtil';

import cmdHelper from 'bpmn-js-properties-panel/lib/helper/CmdHelper';


function getSpellVal(element) {
  debugger
  var bo = getBusinessObject(element);

  var boSpellElement = bo.get('magic:spell');

  var spellVal = '';
  if (typeof boSpellElement !== 'undefined') {
    spellVal = 'bpmn';
  } else {
    spellVal = 'cmmn';
  }

  return spellVal;
}

export default function (group, element, bpmnFactory, translate) {

  // Only return an entry, if the currently selected
  // element is a start event.

  /*if (is(element, 'bpmn:StartEvent')) {debugger
    group.entries.push(entryFactory.textField(translate, {
      id : 'spell',
      description : 'Apply a black magic spell',
      label : 'Spell',
      modelProperty : 'spell'
    }));
  }*/

  if (is(element, 'bpmn:StartEvent')) {
    group.entries.push(entryFactory.selectBox(translate, {
      id: 'spell',
      label: translate('自定义'),
      selectOptions: [
        {name: 'BPMN', value: 'bpmn'},
        {name: 'CMMN', value: 'cmmn'}
      ],
      modelProperty: 'spell',
      description: 'Apply a black magic spell',
      emptyParameter: false,

      get: function (element, node) {
        return {
          spell: getSpellVal(element)
        };
      },

      set: function (element, values) {debugger
        var spellVal = values.spell;
        bpmnFactory.create('magic:spell')
        return cmdHelper.updateBusinessObject(element, {
          spell: spellVal
        });
      }
    }));
  }
}