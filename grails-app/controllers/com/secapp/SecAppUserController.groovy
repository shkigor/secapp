package com.secapp

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured('ROLE_ADMIN')
class SecAppUserController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond SecAppUser.list(params), model: [secAppUserInstanceCount: SecAppUser.count()]
    }

    def show(SecAppUser secAppUserInstance) {
        respond secAppUserInstance
    }

    def create() {
        respond new SecAppUser(params)
    }

    @Transactional
    def save(SecAppUser secAppUserInstance) {
        if (secAppUserInstance == null) {
            notFound()
            return
        }

        if (secAppUserInstance.hasErrors()) {
            respond secAppUserInstance.errors, view: 'create'
            return
        }

        secAppUserInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'secAppUser.label', default: 'SecAppUser'), secAppUserInstance.id])
                redirect secAppUserInstance
            }
            '*' { respond secAppUserInstance, [status: CREATED] }
        }
    }

    def edit(SecAppUser secAppUserInstance) {
        respond secAppUserInstance
    }

    @Transactional
    def update(SecAppUser secAppUserInstance) {
        if (secAppUserInstance == null) {
            notFound()
            return
        }

        if (secAppUserInstance.hasErrors()) {
            respond secAppUserInstance.errors, view: 'edit'
            return
        }

        secAppUserInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'SecAppUser.label', default: 'SecAppUser'), secAppUserInstance.id])
                redirect secAppUserInstance
            }
            '*' { respond secAppUserInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(SecAppUser secAppUserInstance) {

        if (secAppUserInstance == null) {
            notFound()
            return
        }

        secAppUserInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'SecAppUser.label', default: 'SecAppUser'), secAppUserInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'secAppUser.label', default: 'SecAppUser'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }
}
