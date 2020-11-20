export default {
    isShowLoginBox (state, isShow) {
        state.showLoginBox = isShow;
    },
    setStateItem (state, Object) {
        state[Object.name] = Object.data;
    },
    setState (state, Object) {
        var setting = arguments[1];
        for (var i in setting) {
            state[i] = setting[i];
        }
    }
};
