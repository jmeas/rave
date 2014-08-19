/*
 * forgotPasswordEndpoint
 * The endpoint for /auth/forgot-password
 *
 */

define(function(require) {
  var Endpoint = require('../../../util/endpoint');
  var userUtil = require('../../user/user-util');
  var ErrorResponse = require('../../../util/error-response');

  var forgotPasswordEndpoint = new Endpoint({

    url: '/auth/forgot-password',

    authorize: false,

    // Request your forgotten password
    post: function(url, data, headers, params, currentUser) {

      // An email is required
      if (!data.email) {
        return new ErrorResponse(400, 'Missing email address field.');
      }

      // Also, the user must exist
      else if (!userUtil.userExists({email: data.email})) {
        return new ErrorResponse(422, 'User with that email does not exist.');
      }

      // Otherwise, we return the retrieved category
      return [200, {message: 'Email sent.'}];
    }
  });

  return forgotPasswordEndpoint;
});