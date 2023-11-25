package com.kristianskokars.tasky.lib

import com.github.michaelbull.result.Ok

/**
 * This file contains some syntactic sugar for using the Result type to make it easier to read for
 * new developers.
 */

typealias Success = Unit
fun success() = Ok(Unit)
