var mongoose = require('mongoose');
var User = mongoose.model('User');

var sendJSONResponse = function (res, status, content) {
  res.status(status);
  res.json(content);
}

module.exports.usersReadAll = function (req, res) {
  User
    .find({})
    .select('cardNumber username userType displayName emailAddress')
    .exec(function (err, user) {
      if (!user) {
        sendJSONResponse(res, 404, {
          "message": "Users not found."
        });
        return;
      }
      else if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      sendJSONResponse(res, 200, user);
    });
};

module.exports.usersUpdateOne = function (req, res) {
  if (!req.params.cardNumber) {
    sendJSONResponse(res, 404, {
      "message": "Not found, card number is required."
    });
    return;
  }
  
  if (req.payload && req.payload.cardNumber) {
    if ((req.payload.cardNumber != req.params.cardNumber)
        && req.payload.userType != "admin" ) {
      sendJSONResponse(res, 404, {
        "message": "You are not authorized to update this user."
      });
      return;
    }
  }
  else {
    sendJSONResponse(res, 404,  {
      "message": "You are not authorized to update this user."
    });
    return;
  }
  
  User
    .findOne({ "cardNumber": req.params.cardNumber })
    .select('userType')
    .exec(function (err, user) {
      if (!user) {
        sendJSONResponse(res, 404, {
          "message": "Card number not found."
        });
        return;
      } 
      else if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      
      user.userType = req.body.userType;
      user.save(function (err, user) {
        if (err) {
          sendJSONResponse(res, 404, err);
        }
        else {
          sendJSONResponse(res, 200, user);
        }
      });
    });
};
