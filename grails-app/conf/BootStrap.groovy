import com.secapp.*

class BootStrap {

    def init = { servletContext ->
        def adminRole = new SecAppRole('ROLE_ADMIN').save()
        def userRole = new SecAppRole('ROLE_USER').save()

        def testUser = new SecAppUser('me', 'password').save()

        SecAppUserSecAppRole.create testUser, adminRole, true

        assert SecAppUser.count() == 1
        assert SecAppRole.count() == 2
        assert SecAppUserSecAppRole.count() == 1
    }
    def destroy = {
    }
}
