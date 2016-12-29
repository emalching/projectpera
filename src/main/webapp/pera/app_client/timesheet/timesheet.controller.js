(function(){
  angular
    .module('peraApp')
    .controller('timesheetCtrl', timesheetCtrl);
  
  timesheetCtrl.$inject = ['$scope', '$location', 'staffData', 'authentication'];
  function timesheetCtrl ($scope, $location, staffData, authentication) {
    var vm = this;
    vm.isLoggedIn = authentication.isLoggedIn();
    
    if (!vm.isLoggedIn) {
      $location.path('/login');
      return;
    }
    
    vm.cardNumber = authentication.currentUser().cardNumber;
    vm.pageHeader = {
      title: 'User Timesheet',
    };
    vm.message = "Loading your user timesheet...";
    vm.formError = "";
    
    staffData
      .getStaffByCardNumber(vm.cardNumber)
      .success(function (data) {
        vm.message = data[0].cardNumber != "" ? "" : "User Profile not found.";
        vm.data = { staff: data[0] };
        
        $scope.originalTimeInOutList = angular.copy(data[0].timeInOutList);
        
        generateSemiMonthlyDateRange(vm.data.staff.timeInOutList);
      })
      .error(function (err) {
        vm.message = err.message;
      });
    
    vm.semiMonthlyDataRange = [];
    
    generateSemiMonthlyDateRange = function(data) {
      var length = data.length;
      var startDate = new Date(data[0].workDate);
      var endDate = new Date(data[length-1].workDate);
      
      var dd = startDate.getDate();
      var mm = startDate.getMonth() + 1;
      var yyyy = startDate.getFullYear();
      
      if (dd < 16) {
        dd = '01';
      }
      else if (dd <= 31) {
        dd = 16;
      }
      startDate = new Date(yyyy+'/'+mm+'/'+dd);
      
      var currentDate = new Date();
      currentDate.setHours(0,0,0,0);
      if (currentDate > endDate) {
        endDate = currentDate;
      }
      
      var monthEndDates = {
        normal: [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
        leap:   [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
      };
      
      for (var i = 0; startDate <= endDate; startDate.setMonth(startDate.getMonth() + 1)) {
        var dd = startDate.getDate();
        var mm = startDate.getMonth() + 1;
        var yyyy = startDate.getFullYear();
        
        if (mm < 10) {
          mm = '0' + mm;
        }
        
        if (dd == 1) {
          rangeStart = yyyy + '/' + mm + '/01';
          rangeEnd = yyyy + '/' + mm + '/15';
          vm.semiMonthlyDataRange.push(rangeStart + ' - ' + rangeEnd);
          
          if (currentDate >= new Date(rangeStart) && currentDate <= new Date(rangeEnd)) {
            vm.selectedDateRange = vm.semiMonthlyDataRange[i];
          }
          i++;
        }
        
        var isLeapYear = new Date(yyyy, 1, 29).getMonth() == 1;
        if (isLeapYear) {
          dd = monthEndDates.leap[mm-1];
        }
        else {
          dd = monthEndDates.normal[mm-1];
        }
        
        rangeStart = yyyy + '/' + mm + '/16';
        rangeEnd = yyyy + '/' + mm + '/' + dd;
        vm.semiMonthlyDataRange.push(rangeStart + ' - ' + rangeEnd);
        
        if (currentDate >= new Date(rangeStart) && currentDate <= new Date(rangeEnd)) {
          vm.selectedDateRange = vm.semiMonthlyDataRange[i];
        }
        i++;
      }
    };
    
    vm.criteriaMatch = function(selectedDateRange) {
      return function(item) {
        itemDate = new Date(item.workDate);
        rangeStart = new Date(selectedDateRange.substring(0, 10));
        rangeEnd = new Date(selectedDateRange.substring(13));
        
        if (itemDate >= rangeStart && itemDate <= rangeEnd) {
          return true;
        }
      };
    };
    
    $scope.cancel = function() {
      vm.data.staff.timeInOutList = angular.copy($scope.originalTimeInOutList);
      vm.formError = "";
    };
    
    $scope.isValidRow = function(row, inputRemark) {
      var inputCount = 0;
      if (row.timeInOverride) {
        inputCount++;
      }
      if (row.timeOutOverride) {
        inputCount++;
      }
      if (inputRemark) {
        inputCount++;
      }
      if (inputCount != 3 && inputCount != 0) {
        vm.formError = "New Time In, New Time Out, and Remarks must all be provided.";
        return " ";
      }
      if (inputCount == 3 && (row.timeInOverride >= row.timeOutOverride)) {
        vm.formError = "New Time In must be less than New Time Out.";
        return " ";
      }
    };
    
    $scope.updateTimesheet = function() {
      staffData
        .updateTimesheetByCardNumber(vm.cardNumber, {
          timeInOutList: vm.data.staff.timeInOutList
        })
        .success(function (data) {
        })
        .error(function (err) {
          vm.formError = "Your timesheet has not been updated, please try again."
        });
      
      return false;
    };
    
    vm.goBack = function() {
      $location.path('/');
    };
    
    vm.generateTimesheet = function() {
      var period = vm.selectedDateRange.split(" ");
      var printIframe = angular.element("<iframe class='hidden'>");
      var formElement = angular.element("<form>");
      var reportUri = "http://vl29.champ.aero:7070/QuartzSpringMongoDB/rest/reports?cardNumber=" + vm.cardNumber + "&periodStart=" + period[0] + "&periodEnd=" + period[2];
      formElement.attr("action", reportUri);
      formElement.attr("method", "post");
      var contentElement = angular.element("<input>").attr("type", "hidden").attr("name",
              "domContent").val(angular.element('.report-outer-wrapper').html());
      
      //build file name
      var fileName = "123.zip";
      var fileNameElement = angular.element("<input>").attr("type", "hidden").attr("name",
              "fileName").val(fileName);
      formElement.append(contentElement);
      formElement.append(fileNameElement);
      printIframe.append(formElement);
      angular.element('body').append(printIframe);
      formElement.submit();
    };
  }
})();