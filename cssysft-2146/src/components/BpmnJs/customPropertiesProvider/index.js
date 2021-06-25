import CamundaPropertiesProvider from './CamundaPropertiesProvider';
import elementTemplates from 'bpmn-js-properties-panel/lib/provider/camunda/element-templates/index';
import translate from 'diagram-js/lib/i18n/translate/index';

export default {
  __depends__: [
    elementTemplates,
    translate
  ],
  __init__: [ 'propertiesProvider' ],
  propertiesProvider: [ 'type', CamundaPropertiesProvider ]
};