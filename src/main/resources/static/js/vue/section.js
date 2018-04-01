var appStory = new Vue({
  el: '#cafestoryArea',
  data: {
    defaultDate: new Date(),
    currentDate: new Date(),
    dateClasses:[]
  },
  methods: {
    storyDate: function() {
        this.dateClasses = moment(this.currentDate).format('YYYY.MM.DD.').split('').map(function(el) {
            if (el == '.') {
                return 'dot';
            } else {
                return 'n' + el;
            }
        });
    },
    moveStoryDate: function(direction) {
        this.currentDate = new Date(this.currentDate.setDate(this.currentDate.getDate() + direction));
        this.changeButton();
        this.storyDate();
    },
    isLastDate: function() {
        return (this.currentDate.getYear() == this.defaultDate.getYear()
                    && this.currentDate.getMonth() == this.defaultDate.getMonth()
                    && this.currentDate.getDate() == this.defaultDate.getDate());
    },
    changeButton: function() {
        if (this.isLastDate()) {
            $(".btn_next").attr( "disabled", true );
            $(".btn_next").addClass("btn_next_off").removeClass("btn_next");
        }
        else {
            $(".btn_next_off").attr( "disabled", false );
            $(".btn_next_off").addClass("btn_next").removeClass("btn_next_off");
        }
    }
  },
  created: function() {
    this.storyDate();
  }
})

var appCafe = new Vue({
  el: '#category-cafe',
  data: {
    cafes: []
  },
  methods: {
    getCafeByCategory: function(categoryId) {
//      this.categories.forEach(function(el) {
//        $("#category_" + el.id).removeClass("select");
//      });
//      $("#category_" + categoryId).addClass("select");

      this.$http.get('/api/categories/' + categoryId + '/cafes').then(
        response => {
          this.cafes = response.body;
        },
        response => {
          console.log(response);
        });
    },
    formatDate: function(date) {
      return moment(date).format('YYYY.MM.DD');
    }
  },
  created: function () {
    this.getCafeByCategory(1);
  }
})
