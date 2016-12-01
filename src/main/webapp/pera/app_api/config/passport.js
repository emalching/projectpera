var passport = require('passport');
var AtlassianCrowdStrategy = require('passport-atlassian-crowd').Strategy;

var mongoose = require('mongoose');
var User = mongoose.model('User');

passport.use(new AtlassianCrowdStrategy({
/*
    crowdServer: "http://vl29.champ.aero:9095/crowd/",
    crowdApplication: "pera",
    crowdApplicationPassword: "pera",
*/
    crowdServer: "http://crowd.champ.aero:8095/crowd/",
    crowdApplication: "pera",
    crowdApplicationPassword: "pera-prod",
    retrieveGroupMemberships: false
  },
  function (userprofile, done) {
    User.findOne({ username: userprofile.username }, function (err, user) {
      if (err) { return done(err); }
      if (!user) {
        return done(null, false, {
          message: 'User not allowed. Contact the Project Pera team.'
        });
      }
      
      if (!user.emailAddress) {
        user.emailAddress = userprofile.email;
        user.displayName = userprofile.displayName;
        user.save(function (err) {
          if (err) {
            console.log('User email address was not updated. ' + err);
          }
        });
      }
      return done(null, user);
    });
  }
));
