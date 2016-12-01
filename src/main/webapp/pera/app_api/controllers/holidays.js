var mongoose = require('mongoose');
var Holidays = mongoose.model('Holidays');

var sendJSONResponse = function (res, status, content) {
  res.status(status);
  res.json(content);
}

module.exports.holidaysReadAll = function (req, res) {
  Holidays
    .find({})
    .exec(function (err, holidays) {
      if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      sendJSONResponse(res, 200, holidays);
    });
};

module.exports.holidaysUpdateAll = function (req, res) {
  if (req.payload && req.payload.userType != "admin") {
        console.log('chiem p1');
    sendJSONResponse(res, 404, {
      "message": "You are not authorized to update the holiday list."
    });
    return;
  }
  
  Holidays
    .find({}, {'_id': 0})
    .select('holidayList')
    .exec(function (err, holidays) {
      if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      
      Holidays
        .remove({}, function(err) {
          if (err) {
            sendJSONResponse(res, 404, err);
          }
          else {
            var holidays = new Holidays();
            holidays.holidayList = req.body.holidayList;
            holidays.save(function (err, holidays) {
              if (err) {
                sendJSONResponse(res, 404, err);
              }
              else {
                sendJSONResponse(res, 200, holidays);
              }
            });
          }
        });
    });
};
