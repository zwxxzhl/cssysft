'use strict';

import entryFactory from 'bpmn-js-properties-panel/lib/factory/EntryFactory';
import {getBusinessObject, is} from 'bpmn-js/lib/util/ModelUtil';
import cmdHelper from "bpmn-js-properties-panel/lib/helper/CmdHelper";
import userApi from "../../../../api/acl/user";

function getAssigneeVal(element) {
    let bo = getBusinessObject(element);
    let boElement = bo.get('camunda:assignee');

    let val = '';
    if (typeof boElement !== 'undefined') {
        val = boElement;
    } else {
        val = '';
    }

    return val;
}

let userList = [];
const getUserList = () => {
    userApi.select()
      .then((res) => {
          userList = res.data.items;
      })
}
getUserList();


export default function(group, element, translate) {
    if (is(element, 'camunda:Assignable')) {

        /*let userList;
        let temp = sessionStorage.getItem("userlist");
        if (temp) {
            userList = JSON.parse(temp);
        } else {
            userList = await getUserList();
            sessionStorage.setItem("userlist", JSON.stringify(userList));
        }*/

        let selectOptions = [];
        for (const item of userList) {
            selectOptions.push({ name: item.nickName, value: item.username })
        }

        /** custom Assignee修改为下拉框 */
        // Assignee
        group.entries.push(entryFactory.selectBox(translate, {
            id: 'assignee',
            label: translate('Assignee'),
            selectOptions: selectOptions,
            modelProperty: 'assignee',
            emptyParameter: false,

            get: function (element, node) {
                return {
                    assignee: getAssigneeVal(element)
                };
            },

            set: function (element, values) {
                let val = values.assignee;
                return cmdHelper.updateProperties(element, {
                    assignee: val
                });
            }
        }));

        /** custom 去掉用户任务，详情下的其它配置 */
        /*// Candidate Users
        group.entries.push(entryFactory.textField(translate, {
            id : 'candidateUsers',
            label : translate('Candidate Users'),
            modelProperty : 'candidateUsers'
        }));

        // Candidate Groups
        group.entries.push(entryFactory.textField(translate, {
            id : 'candidateGroups',
            label : translate('Candidate Groups'),
            modelProperty : 'candidateGroups'
        }));

        // Due Date
        group.entries.push(entryFactory.textField(translate, {
            id : 'dueDate',
            description : translate('The due date as an EL expression (e.g. ${someDate} or an ISO date (e.g. 2015-06-26T09:54:00)'),
            label : translate('Due Date'),
            modelProperty : 'dueDate'
        }));

        // FollowUp Date
        group.entries.push(entryFactory.textField(translate, {
            id : 'followUpDate',
            description : translate('The follow up date as an EL expression (e.g. ${someDate} or an ' +
                'ISO date (e.g. 2015-06-26T09:54:00)'),
            label : translate('Follow Up Date'),
            modelProperty : 'followUpDate'
        }));

        // priority
        group.entries.push(entryFactory.textField(translate, {
            id : 'priority',
            label : translate('Priority'),
            modelProperty : 'priority'
        }));*/
    }
};
