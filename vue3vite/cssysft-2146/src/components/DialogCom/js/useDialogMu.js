import {inject} from "vue";

export default function useForm(search, form, refDialogMu, refDepForm) {
  const dialogData = inject('dialogData');
  const openType = inject('openType');
  const dialogClose = inject('dialogClose');

  return {
    dialogData,
    openType,
    dialogClose
  }
}

