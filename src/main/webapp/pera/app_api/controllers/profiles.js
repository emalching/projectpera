var mongoose = require('mongoose');
var Staff = mongoose.model('Staff');

var sendJSONResponse = function (res, status, content) {
  res.status(status);
  res.json(content);
}

module.exports.profilesReadOne = function (req, res) {
  if (req.params && req.params.cardNumber) {
    if (req.payload && req.payload.cardNumber) {
      if ((req.params.cardNumber != req.payload.cardNumber)
          && req.payload.userType != 'admin') {
        sendJSONResponse(res, 404, {
          "message": "You are not authorized to view this profile."
        });
        return;
      }
    }
    else {
      sendJSONResponse(res, 404, {
        "message": "You are not authorized to view this profile."
      });
      return;
    }
  }
  else {
    sendJSONResponse(res, 404, {
      "message": "No cardNumber in request."
    });
    return;
  }
  /*
  Staff
    .findOne({ "cardNumber": req.params.cardNumber })
    .select('cardNumber firstName lastName employeeName projectName department position teamLeadName')
    .exec(function (err, staff) {
      if (!staff) {
        sendJSONResponse(res, 404, {
          "message": "Card number not found."
        });
        return;
      }
      else if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      sendJSONResponse(res, 200, staff);
    });
*/

  Staff
    .aggregate()
    .match({
      cardNumber: Number(req.params.cardNumber)})
    .lookup({
      from: "users",
      localField: "cardNumber",
      foreignField:"cardNumber",
      as: "userInfo"})
/*
    .select('cardNumber firstName lastName employeeName projectName department position teamLeadName userInfo')
*/
    .exec(function (err, staff) {
      if (staff.length == 0) {
        sendJSONResponse(res, 404, {
          "message": "Card number not found."
        });
        return;
      }
      else if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      sendJSONResponse(res, 200, staff);
    });
};

module.exports.profilesUpdateOne = function (req, res) {
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
        "message": "You are not authorized to update this profile."
      });
      return;
    }
  }
  else {
    sendJSONResponse(res, 404,  {
      "message": "You are not authorized to update this profile."
    });
    return;
  }
  
  Staff
    .findOne({ "cardNumber": req.params.cardNumber })
    .select('employeeName projectName department position teamLeadName')
    .exec(function (err, staff) {
      if (!staff) {
        sendJSONResponse(res, 404, {
          "message": "Card number not found."
        });
        return;
      } 
      else if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      
      staff.employeeName = req.body.employeeName;
      staff.project = req.body.project;
      staff.department = req.body.department;
      staff.position = req.body.position;
      staff.teamLeadName = req.body.teamLeadName;
      staff.save(function (err, staff) {
        if (err) {
          sendJSONResponse(res, 404, err);
        }
        else {
          sendJSONResponse(res, 200, staff);
        }
      });
    });
};

module.exports.timesheetUpdateOne = function (req, res) {
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
        "message": "You are not authorized to update this timesheet."
      });
      return;
    }
  }
  else {
    sendJSONResponse(res, 404,  {
      "message": "You are not authorized to update this timesheet."
    });
    return;
  }
  
  Staff
    .findOne({ "cardNumber": req.params.cardNumber })
    .select('timeInOutList')
    .exec(function (err, staff) {
      if (!staff) {
        sendJSONResponse(res, 404, {
          "message": "Card number not found."
        });
        return;
      } 
      else if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      
      staff.timeInOutList = req.body.timeInOutList;
      staff.save(function (err, staff) {
        if (err) {
          sendJSONResponse(res, 404, err);
        }
        else {
          sendJSONResponse(res, 200, staff);
        }
      });
    });
};
