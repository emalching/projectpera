var express = require('express');
var router = express.Router();
var jwt = require('express-jwt');
var auth = jwt({
  secret: process.env.JWT_SECRET,
  userProperty: 'payload'
});

var ctrlProfiles = require('../controllers/profiles');
var ctrlAuth = require('../controllers/authentication');
var ctrlUsers = require('../controllers/users');
var ctrlHolidays = require('../controllers/holidays');
var ctrlBatchrun = require('../controllers/batchrun');

router.get('/profile/:cardNumber', auth, ctrlProfiles.profilesReadOne);
router.put('/profile/:cardNumber', auth, ctrlProfiles.profilesUpdateOne);

router.put('/timesheet/:cardNumber', auth, ctrlProfiles.timesheetUpdateOne);

router.post('/register', ctrlAuth.register);
router.post('/login', ctrlAuth.login);

router.get('/admin/users', auth, ctrlUsers.usersReadAll);
router.put('/admin/users/:cardNumber', auth, ctrlUsers.usersUpdateOne);

router.get('/admin/holidays', auth, ctrlHolidays.holidaysReadAll);
router.put('/admin/holidays', auth, ctrlHolidays.holidaysUpdateAll);

router.get('/admin/batchrun', auth, ctrlBatchrun.batchrunReadAll);

module.exports = router;
