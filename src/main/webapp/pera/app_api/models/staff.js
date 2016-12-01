var mongoose = require('mongoose');

var timeInOutSchema = new mongoose.Schema({
  workDate: {
    type: String
  },
  timeIn: {
    type: String
  },
  timeOut: {
    type: String
  },
  timeInOverride: {
    type: String
  },
  timeOutOverride: {
    type: String
  },
  remarks: {
    type: String
  },
});

var staffSchema = new mongoose.Schema({
  cardNumber: {
    type: Number,
    required: true
  },
  firstName: {
    type: String,
  },
  lastName: {
    type: String,
  },
  employeeName: {
    type: String,
    required: true
  },
  department: {
    type: String,
    required: true
  },
  position: {
    type: String,
    required: true
  },
  project: {
    type: String,
    required: true
  },
  teamLeadName: {
    type: String,
    required: true
  },
  timeInOutList: [timeInOutSchema]
}, { collection: 'staff' });

mongoose.model('Staff', staffSchema);
