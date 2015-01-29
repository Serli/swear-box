app.service('membersService', ['$http', function ($http) {
    var urlBase = '/members';

    this.getMembers = function () {
        return $http.get(urlBase);
    };

    this.increaseDebt = function (id) {
        return $http.put(urlBase + '/increase/' + id);
    };

    this.addMember = function (data) {
        return $http.post(urlBase, data);
    };
        
    this.deleteMember = function (id) {
        return $http.delete(urlBase + '/' + id);
    };

    this.dischargeMember = function (id) {
        return $http.put(urlBase + '/discharge/' + id, {});
    };
        
    this.updateNameFirstname = function (id, data) {
        return $http.put(urlBase + '/name/' + id, data);
    };

    this.updatePicture = function (id, fd, data) {
        return $http.put(urlBase + '/picture/' + id, fd, data);
    };
}]);