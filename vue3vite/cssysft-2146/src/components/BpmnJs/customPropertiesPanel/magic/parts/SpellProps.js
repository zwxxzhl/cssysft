import entryFactory from 'bpmn-js-properties-panel/lib/factory/EntryFactory';

import {
  getBusinessObject, is
} from 'bpmn-js/lib/util/ModelUtil';

import cmdHelper from 'bpmn-js-properties-panel/lib/helper/CmdHelper';


function getSpellVal(element) {
  let bo = getBusinessObject(element);
  let boSpellElement = bo.get('magic:spell');

  let spellVal = '';
  if (typeof boSpellElement !== 'undefined') {
    spellVal = boSpellElement;
  } else {
    spellVal = '';
  }

  return spellVal;
}

export default function (group, element, translate) {

  // Only return an entry, if the currently selected
  // element is a start event.

  /*// 输入框自定义方法
  if (is(element, 'bpmn:StartEvent')) {debugger
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

      set: function (element, values) {
        var spellVal = values.spell;
        return cmdHelper.updateProperties(element, {
          spell: spellVal
        });
      }
    }));
  }
}