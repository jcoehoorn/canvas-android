/*
 * Copyright (C) 2019 - present Instructure, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.instructure.student.mobius.assignmentDetails.submissionDetails.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.instructure.canvasapi2.models.CanvasContext
import com.instructure.canvasapi2.models.Course
import com.instructure.canvasapi2.utils.pageview.PageView
import com.instructure.canvasapi2.utils.pageview.PageViewUrlParam
import com.instructure.interactions.router.Route
import com.instructure.interactions.router.RouterParams
import com.instructure.pandautils.utils.*
import com.instructure.student.mobius.assignmentDetails.submissionDetails.*
import com.instructure.student.mobius.common.ui.MobiusFragment

@PageView(url = "{canvasContext}/assignments/{assignmentId}/submissions")
class SubmissionDetailsFragment :
    MobiusFragment<SubmissionDetailsModel, SubmissionDetailsEvent, SubmissionDetailsEffect, SubmissionDetailsView, SubmissionDetailsViewState>() {

    val canvasContext by ParcelableArg<Course>(key = Const.CANVAS_CONTEXT)

    @get:PageViewUrlParam(name = "assignmentId")
    val assignmentId by LongArg(key = Const.ASSIGNMENT_ID)

    override fun makeEffectHandler() = SubmissionDetailsEffectHandler()

    override fun makeUpdate() = SubmissionDetailsUpdate()

    override fun makeView(inflater: LayoutInflater, parent: ViewGroup) =
        SubmissionDetailsView(inflater, parent, canvasContext, childFragmentManager)

    override fun makePresenter() = SubmissionDetailsPresenter

    override fun makeInitModel() = SubmissionDetailsModel(canvasContext = canvasContext, assignmentId = assignmentId)

    companion object {

        @JvmStatic
        fun makeRoute(course: CanvasContext, assignmentId: Long): Route {
            val bundle = course.makeBundle {
                putLong(Const.ASSIGNMENT_ID, assignmentId) }
            return Route(null, SubmissionDetailsFragment::class.java, course, bundle)
        }

        @JvmStatic
        fun validRoute(route: Route): Boolean {
            return route.canvasContext is Course &&
                    (route.arguments.containsKey(Const.ASSIGNMENT_ID) ||
                            route.paramsHash.containsKey(RouterParams.ASSIGNMENT_ID))
        }

        @JvmStatic
        fun newInstance(route: Route): SubmissionDetailsFragment? {
            if (!validRoute(route)) return null

            // If routed from a URL, set the bundle's assignment ID from the url value
            if (route.paramsHash.containsKey(RouterParams.ASSIGNMENT_ID)) {
                val assignmentId = route.paramsHash[RouterParams.ASSIGNMENT_ID]?.toLong() ?: -1
                route.arguments.putLong(Const.ASSIGNMENT_ID, assignmentId)
            }

            return SubmissionDetailsFragment().withArgs(route.arguments)
        }

    }

}
