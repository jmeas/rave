define(function(require) {
  var rave = require('rave');
  rave.config([
    '$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider.state('portal.widgetStore', {
        url: '/app/widget-store',
        templateUrl: '/widget-store/widget-store.html'
      });
    }
  ]);
});
