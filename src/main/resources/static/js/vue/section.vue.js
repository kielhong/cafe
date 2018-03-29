var app = new Vue({
  el: '#category',
  data: {
    categories: [],
    cafes: []
  },
  methods: {
    getCategories: function() {
      this.$http.get('/api/categories').then(
        response => {
          this.categories = response.body;
        });
    },
    getCafeByCategory: function(categoryId) {
      this.categories.forEach(function(el) {
        $("#category_" + el.id).removeClass("select");
      });
      $("#category_" + categoryId).addClass("select");

      this.$http.get('/api/categories/' + categoryId + '/cafes').then(
        response => {
          this.cafes = response.body;
        });
    },
    formatDate: function(date) {
      return moment(date).format('YYYY.MM.DD');
    }
  },
  created: function () {
    this.getCategories();
    this.getCafeByCategory(1);
  }
})