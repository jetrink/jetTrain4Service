import template from "./student.sessionHistory.html";


export default {
    controller: function ($scope,authenticationService) {
        "ngInject";
authenticationService.getStudentHistory().then(function (response) {
    $scope.data = response.data;
})
    },
    template:template
}