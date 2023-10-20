package com.tracker.ui.controllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tracker.commons.models.Events;
import com.tracker.services.dto.PaginationRequestDTO;
import com.tracker.services.impls.EventsService;
import com.tracker.ui.converters.JsonWrapper;
import com.tracker.ui.utils.UserUtils;

@Controller
public class EventsController extends BaseController {

	@Autowired
	private EventsService eventsService;

	@RequestMapping("/events")
	public String events(HttpServletRequest request, HttpServletResponse response) {
		return "events";
	}

	@GetMapping("/rest/eventsList")
	public @ResponseBody JsonWrapper<Events> getMarketPrices(@ModelAttribute final PaginationRequestDTO dto) {

		dto.setPage((dto.getPage() / dto.getSize()) + 1);

		final List<Events> list = eventsService.getEventsList(UserUtils.getLoggedInUserId(), dto);
		final int count = eventsService.getEventsListCount(UserUtils.getLoggedInUserId(), dto);

		return new JsonWrapper<Events>(list, dto.getSize() == -1 ? count : dto.getSize(), count, count);
	}

	@RequestMapping(value = "/viewFiles", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest req, HttpServletResponse response, @QueryParam(value = "fileName") String fileName) throws IOException {

		File file = null;

		file = new File(req.getContextPath()+File.separator+fileName);

		if (!file.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			System.out.println(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();
			return;
		}

		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null) {
			System.out.println("mimetype is not detectable, will take default");
			mimeType = "application/octet-stream";
		}

		System.out.println("mimetype : " + mimeType);

		response.setContentType(mimeType);

		response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", file.getName()));

		response.setContentLength((int) file.length());

		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

		FileCopyUtils.copy(inputStream, response.getOutputStream());
	}

	

}
