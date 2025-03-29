<template lang="html">
  <div class="mind-editor"></div>
</template>

<script>
import {
  mapActions,
  mapMutations,
  mapGetters
} from 'vuex';
import Cookies from 'js-cookie';
import {
  listCase,
  getCase,
  delCase,
  addCase,
  updateCase,
  getCaseGeneralInfo,
  getCountByCondition,
  listCreators,
  getCaseDetail,
  deleteCaseById,
  editCase,
  createOrCopyCase
} from '@/api/manager/case';
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
  data() {
    return {
      previewTimer: null,
      mindText: {} // 改为空对象初始化
    };
  },

  // 父组件中通过provide来提供变量，然后在子组件中通过inject来注入变量
  inject: ['main'],

  mounted() {
    // console.log('this minde:',this.minder)
    var Editor = require('../../script/editor');
    var el = this.$el;
    console.log('=====el====', el);
    var editor = window.editor = new Editor(el);
    this.setEditor(editor);

    const id = this.$route.query.caseId;
    const recordId = this.$route.query.recordId;
    let flg = false;
    if (recordId != null) {
      //获取记录的用例集详情
      console.log("走的recordId记录用例详情");
      getRecordTail(recordId, id)
        .then(res => {
          // 获取当前执行记录的用例caseContent
          const caseContent = JSON.parse(res.data.caseContent);
          editor.minder.importJson(caseContent);
          //如果当前token已失效
         
        })
        .catch(error => {
           MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录，注意：数据可能未成功存储', '系统提示', { 
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => { // 确认回调
              window.location.href = 'http://localhost:80/login?redirect=/index'
            }).catch(() => { // 取消回调
              // 这里不需要任何代码，点击取消会自动关闭弹窗
            });
        });
      // 初始化后设置只读模式
      // window.minder.disable();
      //  // 禁用接收器输入
      // this.receiver.disable();
    } else {
      //获取用例集详情
      console.log("走的id获取用例集详情");
      getCaseDetail(id)
        .then(res => {
           // 获取当前caseId的用例caseContent
          const caseContent = JSON.parse(res.data.caseContent);
          editor.minder.importJson(caseContent);
          
         
        })
        .catch(error => {
            MessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录，注意：数据可能未成功存储', '系统提示', { 
              confirmButtonText: '重新登录',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => { // 确认回调
              window.location.href = 'http://localhost:80/login?redirect=/index'
            }).catch(() => { // 取消回调
              // 这里不需要任何代码，点击取消会自动关闭弹窗
            });
        });
    }

 

    editor.minder.on('contentchange', function () {
      //更新用例内容
      if (recordId != null) {
       
      } else {
        const query = {
          id: id,
          caseContent: JSON.stringify(editor.minder.exportJson())
        };
          //保存最新内容
        updateCase(query)
          .then(res => {
         
          })
          .catch(error => {
            
          });
      }
      window.localStorage.setItem(id, JSON.stringify(editor.minder.exportJson()));
      this.$set(this.mindText, id, editor.minder.exportJson()); // 同步到组件数据
      flg = true;
    }.bind(this));

    window.minder = window.km = editor.minder;
    this.setMinder(editor.minder);
    this.executeCallback();

    // 显示 note 浮层 kityminder-core 源码src/module/note.js NoteIconRenderer
    window.minder.on('shownoterequest', this.shownoterequest);

    // note 浮层隐藏
    document.addEventListener('wheel', this.main.hidePreviewer);
    document.addEventListener('mousedown', this.main.hidePreviewer);
    document.addEventListener('mousewheel', this.main.hidePreviewer);

    this.$once('hook:beforeDestroy', () => {
      document.removeEventListener('wheel', this.main.hidePreviewer);
      document.removeEventListener('mousedown', this.main.hidePreviewer);
      document.removeEventListener('mousewheel', this.main.hidePreviewer);
    });
  },

  computed: {
    ...mapGetters([
      'minder'
    ])
  },

  methods: {
    ...mapActions([
      'executeCallback'
    ]),
    ...mapMutations([
      'setMinder',
      'setEditor'
    ]),

    // 展示 note 的浮层
    shownoterequest(e) {
      this.previewTimer = setTimeout(() => {
        this.main.preview(e.node, e.keyword);
      }, 300);
    },

    hidenoterequest() {
      clearTimeout(this.previewTimer);
    }
  }
};
</script>

<style lang="scss">
  @import "../../style/editor.scss";
</style>