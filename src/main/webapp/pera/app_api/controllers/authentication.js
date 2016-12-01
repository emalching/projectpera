var passport = require('passport');
var mongoose = require('mongoose');
mongoose.Promise = require('bluebird');
var User = mongoose.model('User');

var sendJSONResponse = function (res, status, content) {
  res.status(status);
  res.json(content);
};

module.exports.register = function (req, res) {
  if (!req.body.username || !req.body.cardNumber) {
    sendJSONResponse(res, 400, {
      "message": "All fields required"
    });
    return;
  }
  
  User
    .findOne({ "username": req.body.username })
    .exec(function (err, user) {
      if (user) {
        sendJSONResponse(res, 404, {
          "message": "Username " + req.body.username + " is already registered."
        });
      }
      else {
        User
          .findOne({ "cardNumber": req.body.cardNumber })
          .exec(function (err, user) {
            if (user) {
              sendJSONResponse(res, 404, {
                "message": "Card number " + req.body.cardNumber + " is already registered."
              });
            }
            else {
              var user = new User();
              user.username = req.body.username;
              user.cardNumber = req.body.cardNumber;
              user.save(function (err) {
                if (err) {
                  sendJSONResponse(res, 404, err);
                }
                else {
                  sendJSONResponse(res, 200, user);
                }
              });
            }
          });
      }
    });
};

module.exports.login = function (req, res) {
  if (!req.body.username || !req.body.password) {
    sendJSONResponse(res, 400, {
      "message": "All fields required"
    });
    return;
  }
  
  passport.authenticate('atlassian-crowd', function (err, user, info) {
    if (err) {
      sendJSONResponse(res, 404, err);
      return;
    }
    
    if (user) {
      var token = user.generateJwt();
      sendJSONResponse(res, 200, {
        "user": user,
        "token": token
      });
    } else {
      sendJSONResponse(res, 401, info);
    }
  })(req, res);
};
