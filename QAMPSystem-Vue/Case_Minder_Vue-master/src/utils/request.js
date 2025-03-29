import axios from 'axios';
import Cookies from 'js-cookie';
import { saveAs } from 'file-saver';
import Vuex from 'vuex';
import Vue from 'vue';
// 修改后
import { Notification, MessageBox, Message, Loading } from 'element-ui';

Vue.use(Vuex);

// errorCode.js 内容
const errorCode = {
    '401': '认证失败，无法访问系统资源',
    '403': '当前操作没有权限',
    '404': '访问资源不存在',
    'default': '系统未知错误，请反馈给管理员'
};

// cache.js 内容
const sessionCache = {
    set(key, value) {
        if (!sessionStorage) {
            return;
        }
        if (key != null && value != null) {
            sessionStorage.setItem(key, value);
        }
    },
    get(key) {
        if (!sessionStorage) {
            return null;
        }
        if (key == null) {
            return null;
        }
        return sessionStorage.getItem(key);
    },
    setJSON(key, jsonValue) {
        if (jsonValue != null) {
            this.set(key, JSON.stringify(jsonValue));
        }
    },
    getJSON(key) {
        const value = this.get(key);
        if (value != null) {
            return JSON.parse(value);
        }
    },
    remove(key) {
        sessionStorage.removeItem(key);
    }
};
const localCache = {
    set(key, value) {
        if (!localStorage) {
            return;
        }
        if (key != null && value != null) {
            localStorage.setItem(key, value);
        }
    },
    get(key) {
        if (!localStorage) {
            return null;
        }
        if (key == null) {
            return null;
        }
        return localStorage.getItem(key);
    },
    setJSON(key, jsonValue) {
        if (jsonValue != null) {
            this.set(key, JSON.stringify(jsonValue));
        }
    },
    getJSON(key) {
        const value = this.get(key);
        if (value != null) {
            return JSON.parse(value);
        }
    },
    remove(key) {
        localStorage.removeItem(key);
    }
};

const cache = {
    /**
     * 会话级缓存
     */
    session: sessionCache,
    /**
     * 本地缓存
     */
    local: localCache
};

// ruoyi.js 内容
// 日期格式化
function parseTime(time, pattern) {
    if (arguments.length === 0 || !time) {
        return null;
    }
    const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}';
    let date;
    if (typeof time === 'object') {
        date = time;
    } else {
        if ((typeof time === 'string') && (/^[0-9]+$/.test(time))) {
            time = parseInt(time);
        } else if (typeof time === 'string') {
            time = time.replace(new RegExp(/-/gm), '/').replace('T', ' ').replace(new RegExp(/\.[\d]{3}/gm), '');
        }
        if ((typeof time === 'number') && (time.toString().length === 10)) {
            time = time * 1000;
        }
        date = new Date(time);
    }
    const formatObj = {
        y: date.getFullYear(),
        m: date.getMonth() + 1,
        d: date.getDate(),
        h: date.getHours(),
        i: date.getMinutes(),
        s: date.getSeconds(),
        a: date.getDay()
    };
    const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
        let value = formatObj[key];
        // Note: getDay() returns 0 on Sunday
        if (key === 'a') {
            return ['日', '一', '二', '三', '四', '五', '六'][value];
        }
        if (result.length > 0 && value < 10) {
            value = '0' + value;
        }
        return value || 0;
    });
    return time_str;
}

// 表单重置
function resetForm(refName) {
    if (this.$refs[refName]) {
        this.$refs[refName].resetFields();
    }
}

// 添加日期范围
function addDateRange(params, dateRange, propName) {
    let search = params;
    search.params = typeof (search.params) === 'object' && search.params !== null && !Array.isArray(search.params) ? search.params : {};
    dateRange = Array.isArray(dateRange) ? dateRange : [];
    if (typeof (propName) === 'undefined') {
        search.params['beginTime'] = dateRange[0];
        search.params['endTime'] = dateRange[1];
    } else {
        search.params['begin' + propName] = dateRange[0];
        search.params['end' + propName] = dateRange[1];
    }
    return search;
}

// 回显数据字典
function selectDictLabel(datas, value) {
    if (value === undefined) {
        return "";
    }
    var actions = [];
    Object.keys(datas).some((key) => {
        if (datas[key].value == ('' + value)) {
            actions.push(datas[key].label);
            return true;
        }
    });
    if (actions.length === 0) {
        actions.push(value);
    }
    return actions.join('');
}

// 回显数据字典（字符串数组）
function selectDictLabels(datas, value, separator) {
    if (value === undefined || value.length === 0) {
        return "";
    }
    if (Array.isArray(value)) {
        value = value.join(",");
    }
    var actions = [];
    var currentSeparator = undefined === separator ? "," : separator;
    var temp = value.split(currentSeparator);
    Object.keys(value.split(currentSeparator)).some((val) => {
        var match = false;
        Object.keys(datas).some((key) => {
            if (datas[key].value == ('' + temp[val])) {
                actions.push(datas[key].label + currentSeparator);
                match = true;
            }
        });
        if (!match) {
            actions.push(temp[val] + currentSeparator);
        }
    });
    return actions.join('').substring(0, actions.join('').length - 1);
}

// 字符串格式化(%s )
function sprintf(str) {
    var args = arguments, flag = true, i = 1;
    str = str.replace(/%s/g, function () {
        var arg = args[i++];
        if (typeof arg === 'undefined') {
            flag = false;
            return '';
        }
        return arg;
    });
    return flag ? str : '';
}

// 转换字符串，undefined,null等转化为""
function parseStrEmpty(str) {
    if (!str || str == "undefined" || str == "null") {
        return "";
    }
    return str;
}

// 数据合并
function mergeRecursive(source, target) {
    for (var p in target) {
        try {
            if (target[p].constructor == Object) {
                source[p] = mergeRecursive(source[p], target[p]);
            } else {
                source[p] = target[p];
            }
        } catch (e) {
            source[p] = target[p];
        }
    }
    return source;
};

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
function handleTree(data, id, parentId, children) {
    let config = {
        id: id || 'id',
        parentId: parentId || 'parentId',
        childrenList: children || 'children'
    };

    var childrenListMap = {};
    var nodeIds = {};
    var tree = [];

    for (let d of data) {
        let parentId = d[config.parentId];
        if (childrenListMap[parentId] == null) {
            childrenListMap[parentId] = [];
        }
        nodeIds[d[config.id]] = d;
        childrenListMap[parentId].push(d);
    }

    for (let d of data) {
        let parentId = d[config.parentId];
        if (nodeIds[parentId] == null) {
            tree.push(d);
        }
    }

    for (let t of tree) {
        adaptToChildrenList(t);
    }

    function adaptToChildrenList(o) {
        if (childrenListMap[o[config.id]] !== null) {
            o[config.childrenList] = childrenListMap[o[config.id]];
        }
        if (o[config.childrenList]) {
            for (let c of o[config.childrenList]) {
                adaptToChildrenList(c);
            }
        }
    }
    return tree;
}

/**
 * 参数处理
 * @param {*} params  参数
 */
function tansParams(params) {
    let result = '';
    for (const propName of Object.keys(params)) {
        const value = params[propName];
        var part = encodeURIComponent(propName) + "=";
        if (value !== null && value !== "" && typeof (value) !== "undefined") {
            if (typeof value === 'object') {
                for (const key of Object.keys(value)) {
                    if (value[key] !== null && value[key] !== "" && typeof (value[key]) !== 'undefined') {
                        let params = propName + '[' + key + ']';
                        var subPart = encodeURIComponent(params) + "=";
                        result += subPart + encodeURIComponent(value[key]) + "&";
                    }
                }
            } else {
                result += part + encodeURIComponent(value) + "&";
            }
        }
    }
    return result;
}


// 返回项目路径
function getNormalPath(p) {
    if (p.length === 0 || !p || p == 'undefined') {
        return p;
    }
    let res = p.replace('//', '/');
    if (res[res.length - 1] === '/') {
        return res.slice(0, res.length - 1);
    }
    return res;
}

// 验证是否为blob格式
function blobValidate(data) {
    return data.type !== 'application/json';
}

// auth.js 内容
const TokenKey = 'Admin-Token';

function getToken() {
    return Cookies.get(TokenKey);
}

function setToken(token) {
    return Cookies.set(TokenKey, token);
}

function removeToken() {
    return Cookies.remove(TokenKey);
}

// user.js 转换为 Vuex 模块
const userModule = {
    state: {
        token: getToken(),
        id: '',
        name: '',
        avatar: '',
        roles: [],
        permissions: []
    },
    mutations: {
        SET_TOKEN(state, token) {
            state.token = token;
        },
        SET_ID(state, id) {
            state.id = id;
        },
        SET_NAME(state, name) {
            state.name = name;
        },
        SET_AVATAR(state, avatar) {
            state.avatar = avatar;
        },
        SET_ROLES(state, roles) {
            state.roles = roles;
        },
        SET_PERMISSIONS(state, permissions) {
            state.permissions = permissions;
        }
    },
    actions: {
        // 登录
        login({ commit }, userInfo) {
            const username = userInfo.username.trim();
            const password = userInfo.password;
            const code = userInfo.code;
            const uuid = userInfo.uuid;
            return new Promise((resolve, reject) => {
                // 这里假设 login 函数已经在其他地方定义
                login(username, password, code, uuid).then(res => {
                    setToken(res.token);
                    commit('SET_TOKEN', res.token);
                    resolve();
                }).catch(error => {
                    reject(error);
                });
            });
        },
        // 获取用户信息
        getInfo({ commit }) {
            return new Promise((resolve, reject) => {
                // 这里假设 getInfo 函数已经在其他地方定义
                getInfo().then(res => {
                    const user = res.user;
                    const defAva = '@/assets/images/profile.jpg';
                    const avatar = (user.avatar == "" || user.avatar == null) ? defAva : process.env.VUE_APP_BASE_API + user.avatar;

                    if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
                        commit('SET_ROLES', res.roles);
                        commit('SET_PERMISSIONS', res.permissions);
                    } else {
                        commit('SET_ROLES', ['ROLE_DEFAULT']);
                    }
                    commit('SET_ID', user.userId);
                    commit('SET_NAME', user.userName);
                    commit('SET_AVATAR', avatar);
                    resolve(res);
                }).catch(error => {
                    reject(error);
                });
            });
        },
        // 退出系统
        logOut({ commit }) {
            return new Promise((resolve, reject) => {
                // 这里假设 logout 函数已经在其他地方定义
                logout(this.state.token).then(() => {
                    commit('SET_TOKEN', '');
                    commit('SET_ROLES', []);
                    commit('SET_PERMISSIONS', []);
                    removeToken();
                    resolve();
                }).catch(error => {
                    reject(error);
                });
            });
        }
    }
};

const store = new Vuex.Store({
    modules: {
        user: userModule
    }
});

// request.js 内容
let downloadLoadingInstance;
// 是否显示重新登录
let isRelogin = { show: false };

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8';
// 创建axios实例
const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: process.env.VUE_APP_BASE_API,
    // 超时
    timeout: 10000
});

// request拦截器
service.interceptors.request.use(config => {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === false
    // 是否需要防止数据重复提交
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false
    if (getToken() && !isToken) {
        config.headers['Authorization'] = 'Bearer ' + getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    // get请求映射params参数
    if (config.method === 'get' && config.params) {
        let url = config.url + '?' + tansParams(config.params);
        url = url.slice(0, -1);
        config.params = {};
        config.url = url;
    }
    if (!isRepeatSubmit && (config.method === 'post' || config.method === 'put')) {
        const requestObj = {
            url: config.url,
            data: typeof config.data === 'object' ? JSON.stringify(config.data) : config.data,
            time: new Date().getTime()
        }
        const requestSize = Object.keys(JSON.stringify(requestObj)).length; // 请求数据大小
        const limitSize = 5 * 1024 * 1024; // 限制存放数据5M
        if (requestSize >= limitSize) {
            console.warn(`[${config.url}]: ` + '请求数据大小超出允许的5M限制，无法进行防重复提交验证。')
            return config;
        }
        const sessionObj = cache.session.getJSON('sessionObj')
        if (sessionObj === undefined || sessionObj === null || sessionObj === '') {
            cache.session.setJSON('sessionObj', requestObj)
        } else {
            const s_url = sessionObj.url;                // 请求地址
            const s_data = sessionObj.data;              // 请求数据
            const s_time = sessionObj.time;              // 请求时间
            const interval = 3000000;                       // 间隔时间(ms)，小于此时间视为重复提交
            if (s_data === requestObj.data && requestObj.time - s_time < interval && s_url === requestObj.url) {
                const message = '数据正在处理，请勿重复提交';
                console.warn(`[${s_url}]: ` + message)
                return Promise.reject(new Error(message))
            } else {
                cache.session.setJSON('sessionObj', requestObj)
            }
        }
    }
    return config
}, error => {
    console.log(error)
    return Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    // 获取错误信息
    const msg = errorCode[code] || res.data.msg || errorCode['default']
    // 二进制数据则直接返回
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
        return res.data
    }
    if (code === 401) {
        if (!isRelogin.show) {
            isRelogin.show = true;
            Notification.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', { confirmButtonText: '重新登录', cancelButtonText: '取消', type: 'warning' }).then(() => {
                isRelogin.show = false;
                useUserStore().logOut().then(() => {
                    location.href = '/index';
                })
            }).catch(() => {
                isRelogin.show = false;
            });
        }
        return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    } else if (code === 500) {
        Message({ message: msg, type: 'error' })
        return Promise.reject(new Error(msg))
    } else if (code === 601) {
        Message({ message: msg, type: 'warning' })
        return Promise.reject(new Error(msg))
    } else if (code !== 200) {
        Notification.error({ title: msg })
        return Promise.reject('error')
    } else {
        return Promise.resolve(res.data)
    }
},
    error => {
        console.log('err' + error)
        let { message } = error;
        if (message == "Network Error") {
            message = "后端接口连接异常";
        } else if (message.includes("timeout")) {
            message = "系统接口请求超时";
        } else if (message.includes("Request failed with status code")) {
            message = "系统接口" + message.substr(message.length - 3) + "异常";
        }
        ElMessage({ message: message, type: 'error', duration: 5 * 1000 })
        return Promise.reject(error)
    }
)

// 通用下载方法
export function download(url, params, filename, config) {
    // 修改后
    downloadLoadingInstance = Loading.service({ text: "正在下载数据，请稍候", background: "rgba(0, 0, 0, 0.7)" }); return service.post(url, params, {
        transformRequest: [(params) => { return tansParams(params) }],
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        responseType: 'blob',
        ...config
    }).then(async (data) => {
        const isBlob = blobValidate(data);
        if (isBlob) {
            const blob = new Blob([data])
            saveAs(blob, filename)
        } else {
            const resText = await data.text();
            const rspObj = JSON.parse(resText);
            const errMsg = errorCode[rspObj.code] || rspObj.msg || errorCode['default']
            ElMessage.error(errMsg);
        }
        downloadLoadingInstance.close();
    }).catch((r) => {
        console.error(r)
        ElMessage.error('下载文件出现错误，请联系管理员！')
        downloadLoadingInstance.close();
    })
}

export default service