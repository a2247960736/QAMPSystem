export const actions = {
  changeCount: ({
    commit
  }) => commit('changeCount'),

  increment: ({
    commit
  }) => commit('increment'),

  decrement: ({
    commit
  }) => commit('decrement'),

  incrementIfOdd({
    commit,
    state
  }) {
    if ((state.count + 1) % 2 === 0) {
      commit('increment')
    }
  },

  incrementAsync({
    commit
  }) {
    return new Promise((resolve, reject) => {
      setTimeout(() => {
        commit('increment')
        resolve()
      }, 1000)
    })
  },

  setConfig: ({
    commit
  }) => commit('setConfig'),

  registerEvent: ({
    commit
  }) => commit('registerEvent', callback),

  executeCallback({
    commit,
    state
  }) {
    state.callbackQueue.forEach(function (ele) {
      ele.apply(this, arguments);
    })
  },

  isQuotaExceeded(e) {
    var quotaExceeded = false;
    if (e) {
      if (e.code) {
        switch (e.code) {
          case 22:
            quotaExceeded = true;
            break;
          case 1014:
            // Firefox
            if (e.name === 'NS_ERROR_DOM_QUOTA_REACHED') {
              quotaExceeded = true;
            }
            break;
        }
      } else if (e.number === -2147024882) {
        // Internet Explorer 8
        quotaExceeded = true;
      }
    }
    return quotaExceeded;
    index
  },

  // 修复getMemory方法参数错误
  getMemory({ commit, state }, key) {
      // 移除错误参数data
      var value = window.localStorage.getItem(key);
      return value ? JSON.parse(value) : null; // 修复空值处理
  },
  
  // 修复setMemory方法的状态处理
  setMemory({ commit }, data) {
      try {
          window.localStorage.setItem(data.key, JSON.stringify(data.value));
          commit('changeSave', true); // 更新保存状态
          return true;
      } catch (e) {
          commit('changeSave', false);
          return false;
      }
  },

  removeMemory(key) {
    var value = window.localStorage.getItem(key);
    window.localStorage.removeItem(key);
    return value;
  },

  clearMemory() {
    window.localStorage.clear();
  }
}
