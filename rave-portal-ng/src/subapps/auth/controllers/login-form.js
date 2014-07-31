define(function(require) {
  var auth = require('../auth');
  require('../services/security');

  auth.controller('loginController', [
    '$scope', 'security',
    function($scope, security) {

      // We call this method when they submit the form.
      $scope.submit = function(credentials) {

        // If they haven't included a username or password,
        // then we do nothing. The HTML will show the error messages
        // for us.
        if (!credentials.username && !credentials.password) {
          return;
        }

        // Otherwise, we attempt to log them in by delegating to the
        // securityService. If it's successful, the security service
        // handles the rest for us.
        security.login(credentials)

          // And if it errors, we catch it here. 
          .catch(function() {
            $scope.invalid = true;
          });
      };
    }
  ]);
});