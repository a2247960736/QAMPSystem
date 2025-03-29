<template lang="">
<div class="progress-group" style="width: 135px;">
  <ul style="width: 135px;">
    <ul :disabled="commandDisabled" style="width: 135px;">
      <li v-for="(item, index) in items" class="menu-btn" :class="classArray(index)" @click="execCommand(index)" :title="title(index)"></li>
    </ul>
  </ul>
</div>
</template>

<script>
import {
  mapGetters,
  mapActions
} from 'vuex'
import { 
  listRecord, 
  getRecord, 
  delRecord, 
  addRecord, 
  updateRecord,
  exportRecord,
  createRecord,
  editRecord,
  deleteRecord,
  clearRecord,
  getRecordGeneralInfo,
  getRecordTail,
  updateNowRecord
} from "@/api/manager/record";
import { Notification, MessageBox, Message, Loading } from 'element-ui';
export default {
  name: 'progressBox',
  data() {
    return {
      items: [
        { text: '0' },
        { text: '1' },
        { text: '2' },
        { text: '3' },
        { text: '4' },
       
      ]
    }
  },
  computed: {
    ...mapGetters({
      'minder': 'getMinder'
    }),
    commandDisabled() {
      var minder = this.minder
      minder.on && minder.on('interactchange', function () {
        this.commandValue = minder.queryCommandValue('progress');
      });
      return minder.queryCommandState && minder.queryCommandState('progress') === -1;
    },
  },
  methods: {
    execCommand(index) {
       // 新增值映射逻辑
    const val = {
      0: null,
      1: 1,
      2: 9,
      3: 5,
      4: 4
    }[index];

      this.commandDisabled || this.minder.execCommand('progress', val)
       const id = this.$route.query.caseId;
       //调试
       console.log("id454546465464",id)
       const recordId = this.$route.query.recordId;
        const query = {
          id: recordId,
          caseId: id,
          caseContent: JSON.stringify(editor.minder.exportJson())
          };
//updateNowRecord
            //保存最新内容
           console.log("更新执行记录")
      updateNowRecord(query)
        .then(res => {
        })
        .catch(error => {
            MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录，注意：执行的结果未成功保存！', '系统提示', { 
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => { // 确认回调
              window.location.href = 'http://localhost:80/login?redirect=/index'
            }).catch(() => { // 取消回调
              // 这里不需要任何代码，点击取消会自动关闭弹窗
            });
      });
          
    },
    classArray(index) {
      var isActive = this.minder.queryCommandValue && this.minder.queryCommandValue('progress') == index;
      var sequence = 'progress-' + index;

      // 用数组返回多个class
      var arr = [{
        'active': isActive
      }, sequence]
      return arr
    },
    title(index) {
      switch (index) {
        case 0:
          return '移除进度';
        case 1:
          return '失败';
        case 2:
          return '通过';
        case 3:
          return '阻塞';
        case 4:
          return '不执行';
      }
    }
  },
  created() {}
}
</script>
