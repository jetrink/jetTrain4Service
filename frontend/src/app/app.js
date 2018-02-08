// the global CSS
import '../../node_modules/angular-material/modules/scss/angular-material.scss';
import './bootstrap_customized.scss';
// angular
import angular from "angular";
import "angular-animate";
import "angular-aria";
import "angular-material";
import "angular-messages";
import "angular-ui-router";
import "angular-country-picker";
//js controllers
import authComponent from "./auth/auth.component.js";
import mainLayoutComp from "./content/shared/mainLayoutCtrl.js";
import profileComp from "./content/shared/profileCtrl.js";
import studentDashboardComp from "./content/dashboard/student/studentDashboardCtrl.js";
import profileSetup from './content/profile/profileSetupCtrl.js';
import studentEditProfileComp from "./content/profile/student/studentEditProfileCtrl.js";
import studentPublicProfile from "./content/profile/student/studentPublicProfileCtrl.js";
import studentProfileSetup from "./content/profile/student/studentProfileSetupCtrl.js";
import tutorDashboardCtrl from "./content/dashboard/tutor/tutorDashboardCtrl.js";
import tutorProfileSetup from "./content/profile/tutor/tutorProfileSetupCtrl.js";
import tutorEditProfileComp from "./content/profile/tutor/tutorEditProfileCtrl.js";
import tutorPublicProfileCtrl from "./content/profile/tutor/tutorPublicProfileCtrl.js";
import managerEditProfileComp from "./content/profile/manager/managerEditProfileCtrl.js";
import managerDashboardComp from "./content/dashboard/manager/managerDashboardCtrl.js";
import managerPublicProfile from "./content/profile/manager/managerPublicProfileCtrl.js";
import managerProfileSetup from "./content/profile/manager/managerProfileSetupCtrl.js";
import tutorRequests from "./content/dashboard/tutor/tutorRequestsCtrl.js";
import studentRequests from "./content/dashboard/student/studentRequestsCtrl.js";
import searchTutorComp from "./content/searchTutor/searchTutorCtrl.js";
import notApproveComp from "./content/notApproved/notApprovedCtrl.js";
import managerSessionCtrl from "./content/session/managerSessionHistoryCtrl.js";
import tutorSessionCtrl from "./content/session/tutorSessionHistoryCtrl.js";
import studentSessionCtrl from "./content/session/studentSessionHistoryCtrl.js";
import managerFindTutorComp from "./content/searchTutor/manager.findTutorCtrl.js";
import managerStudentRequestComp from "./content/dashboard/manager/managerRequestCtrl.js";
import videoChatPageComp from "./content/session/videoChatPageCtrl.js";
import adminLogin from "./content/admin/authCtrl.js";
//services
import authService from "./services/authentication.service.js";
import adminPage from "./content/admin/adminPageCtrl.js";
//import layout htmls
import profileLayout from "./content/shared/profileLayout.html";

const app = angular.module("Teach4Service", ["ui.router", "ngMaterial", "ngMessages", "puigcerber.countryPicker"]);
authService.init(app);
app.config(($stateProvider, $urlRouterProvider) => {
	"ngInject";
	$urlRouterProvider.otherwise("/login");

	$stateProvider
		.state("app", {
			template: "<ui-view></ui-view>",
			reload: true,
			abstract: true
		})
		.state("app.auth", {
			url: "/login",
			reload: true,
			controller: authComponent.controller,
			template: authComponent.template,
			requireLogin: false
		})

		.state('app.account', {
			abstract: true,
			reload: true,
			template: mainLayoutComp.template,
			controller: mainLayoutComp.controller,
			requireLogin: true
		})

		.state('app.account.tutor.notapprove', {
			url: "/profile/notapproved",
			reload: true,
			template: notApproveComp.template,
			requireLogin: true
		})

		.state('app.profileSetup', {
			url: '/profilesetup',
			reload: true,
			controller: profileSetup.controller,
			template: profileSetup.template,
			abstract: true,
			requireLogin: true
		})

		.state('app.profileSetup.student', {
			url: '/student',
			reload: true,
			template: studentProfileSetup.template,
			controller: studentProfileSetup.controller,
			requireLogin: true
		})

		.state('app.profileSetup.tutor', {
			url: '/tutor',
			reload: true,
			template: tutorProfileSetup.template,
			controller: tutorProfileSetup.controller,
			requireLogin: true
		})

		.state('app.profileSetup.manager', {
			url: '/manager',
			reload: true,
			template: managerProfileSetup.template,
			controller: managerProfileSetup.controller,
			requireLogin: true
		})

		.state('app.account.profile', {
			abstract: true,
			template: profileLayout,
			reload: true,
			requireLogin: true
		})

		.state('app.account.profile.list', {
			url: '/profile',
			controller: profileComp.controller,
			template: profileComp.template,
			reload: true,
			requireLogin: true,
		})


		//STUDENT ROUTES
		.state('app.account.student', {
			url: '/student',
			template: '<ui-view></ui-view>',
			reload: true,
			abstract: true,
			requireLogin: true
		})
		.state('app.account.student.dashboard', {
			url: '/dashboard',
			controller: studentDashboardComp.controller,
			template: studentDashboardComp.template,
			reload: true,
			requireLogin: true
		})
		.state('app.account.profile.studentEdit', {
			url: '/profile/student/edit',
			template: studentEditProfileComp.template,
			controller: studentEditProfileComp.controller,
			reload: true,
			requireLogin: true
		})
		.state('app.account.profile.studentView', {
			url: '/profile/student/publicview',
			template: studentPublicProfile.template,
			controller: studentPublicProfile.controller,
			reload: true,
			requireLogin: true
		})
		.state('app.account.student.findTutor', {
			url: '/findtutor',
			template: searchTutorComp.template,
			controller: searchTutorComp.controller,
			reload: true,
			requireLogin: true

		})


		.state('app.account.student.requests', {
			url: '/requests',
			template: studentRequests.template,
			controller: studentRequests.controller,
			reload: true,
			requireLogin: true

		})


		.state('app.account.student.sessionHistory', {
			url: '/sessionhistory',
			controller: studentSessionCtrl.controller,
			template: studentSessionCtrl.template,
			reload: true,
			requireLogin: true
		})

		//TUTOR ROUTES
		.state('app.account.tutor', {
			url: '/tutor',
			template: '<ui-view></ui-view>',
			reload: true,
			abstract: true,
			requireLogin: true
		})
		.state('app.account.tutor.dashboard', {
			url: '/dashboard',
			controller: tutorDashboardCtrl.controller,
			template: tutorDashboardCtrl.template,
			reload: true,
			requireLogin: true
		})
		.state('app.account.tutor.requests', {
			url: '/requests',
			template: tutorRequests.template,
			controller: tutorRequests.controller,
			reload: true,
			requireLogin: true
		})

		.state('app.account.profile.tutorEdit', {
			url: '/profile/tutor/edit',
			reload: true,
			template: tutorEditProfileComp.template,
			controller: tutorEditProfileComp.controller,
			requireLogin: true
		})
		.state('app.account.profile.tutorView', {
			url: '/profile/tutor/publicview',
			template: tutorPublicProfileCtrl.template,
			controller: tutorPublicProfileCtrl.controller,
			requireLogin: true
		})
		.state('app.account.tutor.sessionhistory', {
			url: '/session',
			controller: tutorSessionCtrl.controller,
			template: tutorSessionCtrl.template,
			reload: true,
			requireLogin: true
		})


		//MANAGER ROUTES
		.state('app.account.manager', {
			url: '/manager',
			template: '<ui-view></ui-view>',
			reload: true,
			abstract: true,
			requireLogin: true
		})
		.state('app.account.manager.dashboard', {
			url: '/dashboard',
			controller: managerDashboardComp.controller,
			template: managerDashboardComp.template,
			requireLogin: true
		})
		.state('app.account.profile.managerEdit', {
			url: '/profile/manager/edit',
			template: managerEditProfileComp.template,
			controller: managerEditProfileComp.controller,
			requireLogin: true
		})
		.state('app.account.profile.managerView', {
			url: '/profile/manager/publicview',
			template: managerPublicProfile.template,
			controller: managerPublicProfile.controller,
			requireLogin: true
		})
		.state('app.account.manager.requests', {
			url: '/studentrequests',
			template: managerStudentRequestComp.template,
			controller: managerStudentRequestComp.controller,
			reload: true,
			requireLogin: true
		})

		.state('app.account.manager.session', {
			url: '/session',
			controller: managerSessionCtrl.controller,
			template: managerSessionCtrl.template,
			reload: true,
			requireLogin: true
		})
		.state('app.account.manager.findTutor', {
			url: '/findtutor',
			template: managerFindTutorComp.template,
			controller: managerFindTutorComp.controller,
			requireLogin: true,
			reload: true
		})

		//Session Routes
		.state('app.session', {
			url: "/session/{sessionId:int}",
			template: videoChatPageComp.template,
			controller: videoChatPageComp.controller,
			requireLogin: true,
			reload: true
		})
		.state('app.session.tutor', {
			url: "/tutor",
			data: {isTutor: true},
			requireLogin: true,
			reload: true
		})
		.state('app.session.student', {
			url: "/student",
			data: {isTutor: false},
			requireLogin: true,
			reload: true
		})

		//ADMIN ROUTES
		.state('app.admin', {
			url: '/admin',
			template: "<ui-view></ui-view>",
			abstract: true,
			reload: true,
			requireLogin: false
		})

		.state('app.admin.login', {
			url: '/login',
			template: adminLogin.template,
			controller: adminLogin.controller,
			reload: true,
			requireLogin: false
		})

		.state('app.admin.home', {
			url: '/home',
			template: adminPage.template,
			controller: adminPage.controller,
			reload: true,
			requireLogin: true
		})


	/*.state('app.account.admin.admin', {
		url: '/session',
		controller: adminCtrl.controller,
		template: adminCtrl.template,
		reload: true,
		requireLogin: true
	})*/

});

app.factory('authHttpResponseInterceptor', ['$q', '$location', function ($q, $location) {
	return {
		response: function (response) {
			if (response.status === 401) {
				//console.log("Response 401");
			}
			return response || $q.when(response);
		},
		responseError: function (rejection) {
			if (rejection.status === 401) {
				localStorage.removeItem("_userToken");
				$location.path('/login').search('returnTo', $location.path());
			}
			return $q.reject(rejection);
		}
	}
}])
	.config(['$httpProvider', function ($httpProvider) {
		//Http Intercpetor to check auth failures for xhr requests
		$httpProvider.interceptors.push('authHttpResponseInterceptor');
	}]);

app.run(function ($http, $rootScope, $state, authenticationService) {
	"ngInject"
	$rootScope.$on('$stateChangeStart', function (event, toState, fromState) {
		var requireLogin = toState.requireLogin;
		if (requireLogin) {
			if (authenticationService.getUserToken() == null) {
				event.preventDefault();
				$state.go('app.auth');
			}
			else {
				if (!authenticationService.getUserToken().systemUser) {

					authenticationService.isApproved().then(function (response) {
						authenticationService.setApproved(response.data);
						authenticationService.getSelf().then(function (response) {
							authenticationService.setUserInfo(response.data);
							if (!authenticationService.getUserInfo().isApproved && toState.name.includes("tutor")) {
								event.preventDefault();
								$state.go("app.account.tutor.notapprove");
							} else {
								if (authenticationService.getUserInfo().isApproved && toState.name.includes("notapprove")) {
									event.preventDefault();
									$state.go("app.account.profile.list");
								}
							}
							if (authenticationService.getUserInfo().isSetup && !toState.name.includes('profileSetup')) {
								event.preventDefault();
							}
							else {

								var userType = toState.name;
								if (!authenticationService.isAuthorized(userType)) {
									event.preventDefault();
								}
							}

						}, function () {
							event.preventDefault();
							authenticationService.logout();
						})
					})

				} else {
					$state.go("app.admin.home");
				}


			}


		} else {
			if (authenticationService.getUserToken() != null) {
				event.preventDefault();
				if (authenticationService.getUserToken().systemUser) {
					$state.go("app.admin.home");
				} else {
					$state.go("app.account.profile.list");
				}
			}
		}

	});
});

export default app;
