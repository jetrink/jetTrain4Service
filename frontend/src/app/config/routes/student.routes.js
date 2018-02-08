'use strict';
export default {
	addRoutes: (stateProvider) => {
		stateProvider
			.state('app.account.student', {
				url: '/student',
				template: '<ui-view></ui-view>',
				abstract: true,
				requireLogin: true
			})
			.state('app.account.student.dashboard', {
				url: '/dashboard',
				controller: 'studentDashboardCtrl',
				templateUrl: 'content/dashboard/student/student.dashboard.html',
				requireLogin: true
			})
			.state('app.account.profile.student', {
				url: '/profile/student',
				templateUrl: 'content/profile/student/student.editProfile.html',
				requireLogin: true
			})
			.state('app.account.student.findTutor', {
				url: '/findtutor',
				template: '<h1>Search for a tutor</h1>',
				requireLogin: true

			})
			.state('app.account.student.requests', {
				url: '/requests',
				template: '<h1>Student requests</h1>',
				requireLogin: true

			})
			.state('app.account.student.sessionHistory', {
				url: '/sessionhistory',
				template: '<h1>Student session history</h1>',
				requireLogin: true

			});
	}
};
