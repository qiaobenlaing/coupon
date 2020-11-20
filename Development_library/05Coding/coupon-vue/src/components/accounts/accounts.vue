<template>
  <div id="accounts">
    <button @click="settlement">买单</button>
    <v-nav></v-nav>
  </div>
</template>
<script>
import nav from '@/components/nav/nav.vue';
export default{
    data() {
        return {
            storeCode: 'test1'
        };
    },
    computed: {
    },
    watch: {
    },
    methods: {
        settlement() {
            this.$router.push({path: `/settlement/${this.storeCode}`});
        }
    },
    created () {
    },
    components: {
        'v-nav': nav
    }
};
</script>
<style lang="stylus" rel="stylesheet/stylus">

</style>
