define(function(require) {
  require('./home');
  
  var angular = require('angular');

  angular.module('home').controller('homeController', function($http) {
    // $http.get('/api/v1/status').success( function(data, responseCode, headers, config) {
    //   console.log('got data for /status', data);
    // });
  });
});