var mongoose = require('mongoose');

var batchrunSchema = new mongoose.Schema({
  batchJob: {
    type: String,
    required: true
  },
  startTime: {
    type: String,
    required: true
  },
  endTime: {
    type: String,
    required: true
  },
  status: {
    type: String,
    required: true
  },
  errorMessage: {
    type: String
  }
}, { collection: 'batchRun' });

mongoose.model('Batchrun', batchrunSchema);
