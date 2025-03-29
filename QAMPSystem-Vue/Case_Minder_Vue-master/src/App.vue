<template>
<div id="app">
    <div class="right-container">
      <router-view class="main-editor"></router-view>
    </div>
  </div>
</template>

<script>
import Cookies from 'js-cookie'
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
  createOrCopyCase,
} from '@/api/manager/case';
// 修改后
import { Notification, MessageBox, Message, Loading } from 'element-ui';
export default {
  name: 'app',
  
   created() {
    // 从URL query中获取token
    const token = this.$route.query.token;
   
    // console.log("-----------------------15616516516-----------------"+token)
    // 1、存储到cookie
    Cookies.set('Admin-Token', token);
    const id = this.$route.query.caseId;
 
    
  },
  data() {
    return {
      isLeftPanelVisible: true
    }
  },
  computed: {
    panelPosition() {
      return this.isLeftPanelVisible ? '280px' : '0'
    }
  },
  methods: {
    toggleLeftPanel() {
      this.isLeftPanelVisible = !this.isLeftPanelVisible
    }
  }
}
</script>

<style>
  @import "./style/normalize.css";
#app {
  height: 100vh;
  position: relative;
}
/* 
.left-panel {
  width: 280px;
  border-right: 1px solid #ebeef5;
  background: #f8f9fa;
  transition: transform 0.3s ease;
} */
/* 
.right-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  transition: margin-left 0.3s ease;
} */
/* 
.main-editor {
  flex: 1;
  position: relative;
  overflow: hidden;
} */

/* .property-panel {
  height: 200px;  
  border-top: 1px solid #ebeef5;
  background: #fff;
} */
/* 
.collapse-btn {
  position: fixed;
  top: 50%;
  z-index: 1000;
  transform: translateY(-50%);
  background: #f8f9fa;
  padding: 12px 6px;
  border-radius: 0 4px 4px 0;
  box-shadow: 2px 0 8px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s ease;
} */
/* 
.collapse-btn:hover {
  background: #409eff;
  color: white;
}

.panel-slide-enter-active,
.panel-slide-leave-active {
  transition: transform 0.3s ease;
} */
</style>
