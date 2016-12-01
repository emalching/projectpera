var mongoose = require('mongoose');
var Batchrun = mongoose.model('Batchrun');

var sendJSONResponse = function (res, status, content) {
  res.status(status);
  res.json(content);
}

module.exports.batchrunReadAll = function (req, res) {
  Batchrun
    .find({})
    .select('batchJob startTime endTime status errorMessage')
    .exec(function (err, batchrun) {
      if (!batchrun) {
        sendJSONResponse(res, 404, {
          "message": "Batch run history not found."
        });
        return;
      }
      else if (err) {
        sendJSONResponse(res, 400, err);
        return;
      }
      sendJSONResponse(res, 200, batchrun);
    });
};
