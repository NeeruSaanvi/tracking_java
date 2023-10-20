/*******************************************************************************
 * Copyright (c) 2018 by Tracker.                                    
 *  All rights reserved.                                                       
 *                                                                             
 *  This software is the confidential and proprietary information of Valley 
 *  Med Trans ("Confidential Information"). You shall not disclose such    
 *  Confidential Information and shall use it only in accordance with the      
 *  terms of the license agreement you entered with Tracker.
 ******************************************************************************/
package com.tracker.rest.converters;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.tracker.commons.annotations.CustomResponse; 

public class CustomJsonConverter extends AbstractGenericHttpMessageConverter<Object> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	public static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm";

	private Gson gsonWritter;
	private Gson gsonReader;

	private String jsonPrefix;

	/**
	 * Construct a new {@code GsonHttpMessageConverter}.
	 */
	public CustomJsonConverter() {
		super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
		this.setDefaultCharset(DEFAULT_CHARSET);

		final JsonDeserializer<Integer> integerDeserializer = new JsonDeserializer<Integer>() {
			@Override
			public Integer deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
					throws JsonParseException {
				if (json == null) {
					return null;
				}

				try {
					return Integer.parseInt(json.getAsString());
				} catch (final Exception e) {
					return null;
				}
			}
		};

		final JsonDeserializer<Long> longDeserializer = new JsonDeserializer<Long>() {
			@Override
			public Long deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
					throws JsonParseException {
				if (json == null) {
					return null;
				}

				try {
					return Long.parseLong(json.getAsString());
				} catch (final Exception e) {
					return null;
				} 
			}
		};

		final JsonSerializer<Date> dateSerializer = new JsonSerializer<Date>() {
			@Override
			public JsonElement serialize(final Date src, final Type typeOfSrc, final JsonSerializationContext context) {
				return src == null ? null : new JsonPrimitive(new SimpleDateFormat(DATE_FORMAT).format(src));
			}
		};


		final JsonDeserializer<Date> dateDeserializer = new JsonDeserializer<Date>() {
			@Override
			public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
					throws JsonParseException {
				try {
					return json == null ? null : new Timestamp(json.getAsLong());
				}
				catch(Exception e) {
					return json == null ? null : new Date(json.getAsLong());
				}
			}
		};

		final JsonDeserializer<Timestamp> timestampDeserializer = new JsonDeserializer<Timestamp>() {
			@Override
			public Timestamp deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
					throws JsonParseException {
				try {
					return json == null ? null : new Timestamp(new SimpleDateFormat(DATE_FORMAT).parse(json.getAsString()).getTime());
				} catch (ParseException e) {
					try {
						return new Timestamp(new SimpleDateFormat(DATE_FORMAT1).parse(json.getAsString()).getTime());
					} catch (ParseException e1) {
						return null;
					}
				}
			}
		};

		final JsonSerializer<Timestamp> timestampSerializer = new JsonSerializer<Timestamp>() {
			@Override
			public JsonElement serialize(final Timestamp src, final Type typeOfSrc, final JsonSerializationContext context) {
				return src == null ? null : new JsonPrimitive(new SimpleDateFormat(DATE_FORMAT).format(src));
			}
		}; 

		final JsonSerializer<BigDecimal> bigDecimalSerializer = new JsonSerializer<BigDecimal>() {
			@Override
			public JsonElement serialize(final BigDecimal src, final Type typeOfSrc, final JsonSerializationContext context) {
				if( src == null ) {
					return null; 
				} 

				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(8);
				df.setMinimumFractionDigits(8);
				df.setGroupingUsed(false); 

				return new JsonPrimitive(df.format(src.setScale(8, BigDecimal.ROUND_DOWN)));
			}
		};

		this.gsonWritter = new GsonBuilder()
				.serializeNulls() 
				//.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(Date.class, dateSerializer).registerTypeAdapter(Date.class, dateDeserializer)
				.registerTypeAdapter(Integer.class, integerDeserializer)
				.registerTypeAdapter(int.class, integerDeserializer).registerTypeAdapter(Long.class, longDeserializer)
				.registerTypeAdapter(long.class, longDeserializer)
				.registerTypeAdapter(Timestamp.class, timestampSerializer).registerTypeAdapter(Timestamp.class, timestampDeserializer)
				.registerTypeAdapter(BigDecimal.class, bigDecimalSerializer)
				.addSerializationExclusionStrategy(new ExclusionStrategy() {//Skip any field that has name 'password'
					@Override
					public boolean shouldSkipField(final FieldAttributes f) {
						return f.getName().toLowerCase().equals("password");
					}

					@Override
					public boolean shouldSkipClass(final Class<?> aClass) {
						return false;
					}
				})
				.create(); 

		this.gsonReader = new GsonBuilder()
				.serializeNulls() 
				//.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.registerTypeAdapter(Date.class, dateSerializer).registerTypeAdapter(Date.class, dateDeserializer)
				.registerTypeAdapter(Integer.class, integerDeserializer)
				.registerTypeAdapter(int.class, integerDeserializer).registerTypeAdapter(Long.class, longDeserializer)
				.registerTypeAdapter(long.class, longDeserializer)
				.registerTypeAdapter(Timestamp.class, timestampSerializer).registerTypeAdapter(Timestamp.class, timestampDeserializer) 
				.create(); 
	}

	@Override
	protected boolean supports(final Class<?> clazz) {
		/*
		 This hack is needed in order to make Swagger works with our CustomJsonConverter.
		 Swagger need to use only native Converter 'MappingJackson2HttpMessageConverter' and can't work with any other converters. 
		 */
		if (clazz.getCanonicalName().contains("springfox.documentation")) {
			return false;
		}

		return true;
	}

	/**
	 * Set the {@code Gson} instance to use. If not set, a default
	 * {@link Gson#Gson() Gson} instance is used.
	 * <p>
	 * Setting a custom-configured {@code Gson} is one way to take further
	 * control of the JSON serialization process.
	 */
	public void setGson(final Gson gson) {
		Assert.notNull(gson, "'gson' is required");
		this.gsonReader = gson;
	}

	/**
	 * Return the configured {@code Gson} instance for this converter.
	 */
	public Gson getGson() {
		return this.gsonReader;
	}

	/**
	 * Specify a custom prefix to use for JSON output. Default is none.
	 * 
	 * @see #setPrefixJson
	 */
	public void setJsonPrefix(final String jsonPrefix) {
		this.jsonPrefix = jsonPrefix;
	}

	/**
	 * Indicate whether the JSON output by this view should be prefixed with
	 * ")]}', ". Default is {@code false}.
	 * <p>
	 * Prefixing the JSON string in this manner is used to help prevent JSON
	 * Hijacking. The prefix renders the string syntactically invalid as a
	 * script so that it cannot be hijacked. This prefix should be stripped
	 * before parsing the string as JSON.
	 * 
	 * @see #setJsonPrefix
	 */
	public void setPrefixJson(final boolean prefixJson) {
		this.jsonPrefix = (prefixJson ? ")]}', " : null);
	}

	@Override
	public Object read(final Type type, final Class<?> contextClass, final HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		final TypeToken<?> token = getTypeToken(type);
		return readTypeToken(token, inputMessage);
	}

	@Override
	protected Object readInternal(final Class<?> clazz, final HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		final TypeToken<?> token = getTypeToken(clazz);
		return readTypeToken(token, inputMessage);
	}

	/**
	 * Return the Gson {@link TypeToken} for the specified type.
	 * <p>
	 * The default implementation returns {@code TypeToken.get(type)}, but this
	 * can be overridden in subclasses to allow for custom generic collection
	 * handling. For instance:
	 * 
	 * <pre class="code">
	 * protected TypeToken<?> getTypeToken(Type type) {
	 * 	if (type instanceof Class && List.class.isAssignableFrom((Class<?>) type)) {
	 * 		return new TypeToken<ArrayList<MyBean>>() {
	 * 		};
	 * 	} else {
	 * 		return super.getTypeToken(type);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * @param type
	 *            the type for which to return the TypeToken
	 * @return the type token
	 * @deprecated as of Spring Framework 4.3.8, in favor of signature-based
	 *             resolution
	 */
	@Deprecated
	protected TypeToken<?> getTypeToken(final Type type) {
		return TypeToken.get(type);
	}

	private Object readTypeToken(final TypeToken<?> token, final HttpInputMessage inputMessage) throws IOException {
		final Reader json = new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
		try {
			return this.gsonReader.fromJson(json, token.getType());
		} catch (final JsonParseException ex) {
			throw new HttpMessageNotReadableException("JSON parse error: " + ex.getMessage(), ex, null);
		}
	}

	private Charset getCharset(final HttpHeaders headers) {
		if (headers == null || headers.getContentType() == null || headers.getContentType().getCharset() == null) {
			return DEFAULT_CHARSET;
		}
		return headers.getContentType().getCharset();
	}

	@Override
	protected void writeInternal(final Object o, final Type type, final HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		Object obj = o;

		if (type != null) {
			// This is to handle list of model object and model having
			// annotation
			if (o instanceof List) {
				final List<?> childObj = (List<?>) o;

				if (!childObj.isEmpty()
						&& findAnnotation(CustomResponse.class, childObj.get(0).getClass().getAnnotations()) != null) {
					obj = new JsonWrapper<>(childObj);
				}
			} else if (findAnnotation(CustomResponse.class, o.getClass().getAnnotations()) != null) {
				obj = new JsonWrapper<>(o);
			}
		}

		/*
		 * if (type != null && findAnnotation(CustomResponse.class,
		 * o.getClass().getAnnotations()) != null) { obj = new JsonWrapper(o); }
		 */

		final Charset charset = getCharset(outputMessage.getHeaders());
		final OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
		try {
			if (this.jsonPrefix != null) {
				writer.append(this.jsonPrefix);
			}
			/*
			 * if (type != null) { this.gson.toJson(obj, type, writer); }
			 */
			else {
				this.gsonWritter.toJson(obj, writer);
			}
			writer.close();
		} catch (final JsonIOException ex) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	protected Annotation findAnnotation(final Class<? extends Annotation> clazz, final Annotation[] annotations) {
		for (final Annotation a : annotations) {
			if (clazz.isAssignableFrom(a.getClass())) {
				return a;
			}
		}
		return null;
	}

}
