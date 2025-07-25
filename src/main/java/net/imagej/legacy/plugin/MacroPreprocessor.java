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

import net.imagej.legacy.IJ1Helper;
import net.imagej.legacy.LegacyService;

import org.scijava.Priority;
import org.scijava.convert.ConvertService;
import org.scijava.module.Module;
import org.scijava.module.ModuleItem;
import org.scijava.module.process.AbstractPreprocessorPlugin;
import org.scijava.module.process.PreprocessorPlugin;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

/**
 * Populates the module's input values, when the command is invoked from an
 * ImageJ 1.x macro.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = PreprocessorPlugin.class,
	priority = 2 * Priority.VERY_HIGH)
public class MacroPreprocessor extends AbstractPreprocessorPlugin {

	@Parameter(required = false)
	private LegacyService legacyService;

	@Parameter
	private ConvertService convertService;

	// -- ModuleProcessor methods --

	@Override
	public void process(final Module module) {
		if (legacyService == null) return;
		final IJ1Helper ij1Helper = legacyService.getIJ1Helper();
		if (ij1Helper == null) return;
		if (!ij1Helper.isMacro()) return;
		for (final ModuleItem<?> input : module.getInfo().inputs()) {
			final String name = input.getName();
			final String value = ij1Helper.getMacroParameter(name);
			if (value == null) {
				// no macro parameter value provided
				continue;
			}
			final Class<?> type = input.getType();
			if (!convertService.supports(value, type)) {
				// cannot convert macro value into the input's actual type
				continue;
			}
			final Object converted = convertService.convert(value, type);
			module.setInput(name, converted);
			module.resolveInput(name);
		}
	}

}
