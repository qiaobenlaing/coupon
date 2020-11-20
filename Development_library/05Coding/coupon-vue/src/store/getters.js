export default {
    doneTodosCount: state => {
        return state.todos.filter(todo => todo.done);
    },
    filterUserCouponList: (state) => (id) => {
        return state.userCouponList.filter(item => item.status === id);
    }
};
