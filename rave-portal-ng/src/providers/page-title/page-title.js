/*
 * pageTitle
 * A directive that updates our page title
 * based on what the router has specified.
 *
 */

define(function(require) {
  var ng = require('angular');

  var pageTitle = ng.module('pageTitle', []);

  pageTitle.directive('updateTitle', [
    '$rootScope', '$state',
    function($rootScope, $state) {
      // console.log('la', $state);
      return {
        link: function(scope, element) {
          var listener = function(event, toState) {
            // var fullState = $state.get(toState.name);
            // console.log('Full state', fullState);
            console.log('current state', $state.$current);
            // console.log('going to', toState);
            var title = 'Default Title';
            if (toState.data && toState.data.pageTitle) {
              title = toState.data.pageTitle;
            }
            element.text(title);
          };

          $rootScope.$on('$stateChangeStart', listener);
        }
      };
    }
  ]);

  return pageTitle;
});
