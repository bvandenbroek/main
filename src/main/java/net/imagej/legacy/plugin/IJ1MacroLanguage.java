/*
 * #%L
 * ImageJ2 software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2025 ImageJ2 developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.legacy.plugin;

import java.util.Arrays;
import java.util.List;

import javax.script.ScriptEngine;

import net.imagej.legacy.LegacyService;

import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.script.AbstractScriptLanguage;
import org.scijava.script.ScriptLanguage;

/**
 * Implements a factory for the ImageJ 1.x Macro language engine.
 * 
 * @author Johannes Schindelin
 */
@Plugin(type = ScriptLanguage.class, name = "ImageJ Macro")
public class IJ1MacroLanguage extends AbstractScriptLanguage {

	@Parameter(required = false)
	private LegacyService legacyService;

	@Override
	public List<String> getNames() {
		return Arrays.asList("ij1", "imagej1");
	}

	@Override
	public List<String> getExtensions() {
		return Arrays.asList("ijm");
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new IJ1MacroEngine(legacyService().getIJ1Helper());
	}

	private LegacyService legacyService() {
		if (legacyService != null) return legacyService;
		synchronized (this) {
			if (legacyService != null) return legacyService;
			legacyService = getContext().getService(LegacyService.class);
			if (legacyService == null) {
				throw new RuntimeException("No legacy service available!");
			}
			return legacyService;
		}
	}

}
