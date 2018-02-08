import template from "./tutor.sessionHistory.html";


export default {

        controller: function ($scope,authenticationService) {
            "ngInject";
            authenticationService.getTutorHistory().then(function (response) {
                $scope.data = response.data;
            })
        },
    template:template
}