import {defineProps} from "vue";

export default function userPropsTableList() {
    const props = defineProps({
        size: {
            type: String,
            required: false,
            default: ''
        },
        stripe: {
            type: Boolean,
            required: false,
            default: false
        },
        border: {
            type: Boolean,
            required: false,
            default: false
        },
        style: {
            type: String,
            required: false,
            default: 'width: 100%'
        },
        height: {
            type: Number,
            required: false,
            default: null
        },
        maxHeight: {
            type: Number,
            required: false,
            default: null
        },
        headerRowClassName: {
            type: Function,
            required: false,
            default: () => {
            }
        },
        headerCellClassName: {
            type: Function,
            required: false,
            default: () => {
            }
        },
        rowClassName: {
            type: Function,
            required: false,
            default: () => {
            }
        },
        cellClassName: {
            type: Function,
            required: false,
            default: () => {
            }
        },
        highlightCurrentRow: {
            type: Boolean,
            required: false,
            default: false
        },
        rowKey: {
            type: String,
            required: true
        },
        tableData: {
            type: Array,
            required: false,
            default: () => ([])
        },
        tableColumn: {
            type: Array,
            required: false,
            default: () => ([])
        },
        rules: {
            type: Object,
            required: false,
            default: () => ({})
        }
    })

    return {
        props
    }
}