var mongoose = require('mongoose');

var holidaySchema = new mongoose.Schema({
  date: {
    type: String,
    unique: true,
    required: true
  },
  holiday: {
    type: String,
    required: true
  }
}, {
  _id: false
});

var holidaysSchema = new mongoose.Schema({
  holidayList: [holidaySchema]
}, { collection: 'holidays' });

mongoose.model('Holidays', holidaysSchema);
