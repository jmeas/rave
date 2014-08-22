/*
 * routes
 * Routes for this subapp. Rave uses the Angular-UI UI-Router
 * library for routing, so be sure to familiarize yourself
 * with that library.
 *
 */

define(function(require) {
  var widgetsCtrl = require('./controllers/widgets');

  return ['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
      $stateProvider
        .state('portal.admin.widgets', {
          url: '/widgets?page',
          templateUrl: '/subapps/admin/widgets/templates/widgets.html',
          authenticate: true,
          controller: widgetsCtrl,
          resolve: {
            widgetsList: ['widgetsResource', '$stateParams',
              function(widgetsResource, $stateParams) {
                return widgetsResource.get({
                  page: $stateParams.page
                });
              }
            ]
          }
        })
        .state('portal.admin.widgets.detail', {
          url: '/widgets/detail-:id',
          templateUrl: '/subapps/admin/widgets/templates/detail.html',
          authenticate: true
        });
    }
  ];
});
