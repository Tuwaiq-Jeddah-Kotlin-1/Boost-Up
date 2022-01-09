package com.mahila.motivationalQuotesApp.utils

import org.junit.Test

import org.junit.Assert.*

class ValidationUtilTest {

   @Test
   fun isValidPassword_return_false_when_password_length_less_than_minimum_length() {
       assertFalse( ValidationUtil.isValidPassword("*Mm5m", 8, 30))
       assertFalse( ValidationUtil.isValidPassword("M1m*m", 8, 30))
   }

    @Test
    fun isValidPassword_return_false_when_password_does_not_contain_special_character() {
        assertFalse( ValidationUtil.isValidPassword("11aHmed4", 8, 30,
           ))
    }


    @Test
    fun iisValidPassword_return_false_when_password_does_not_contain_number() {
        assertFalse( ValidationUtil.isValidPassword("Ah#medl", 8, 30
           ))
    }

    @Test
    fun isValidPassword_return_false_when_password_does_not_contain_capital_letters() {
        assertFalse( ValidationUtil.isValidPassword("*2hhmedd", 8, 30
           ))
    }
    @Test
    fun isValidPassword_return_true_when_password_meet_all_conditions() {
        assertTrue( ValidationUtil.isValidPassword("1M*mmmmm", 8, 30
           ))
        assertTrue( ValidationUtil.isValidPassword("Ah11med@1", 8, 30
           ))

        assertTrue( ValidationUtil.isValidPassword("ah12Med@1", 8, 30
           ))
    }

    @Test
    fun isValidEmail_return_true_when_email_is_valid(){
        assertTrue(ValidationUtil.isValidEmail("ma@gmail.com"))
        assertTrue(ValidationUtil.isValidEmail("user0@stu.kau.edu.sa"))
    }
    @Test
    fun isValidEmail_return_false_when_email_prefix_is_not_valid(){
        assertFalse(ValidationUtil.isValidEmail("userName  @kau.sa"))
        assertFalse(ValidationUtil.isValidEmail("name..def@gmail.com"))
        assertFalse(ValidationUtil.isValidEmail("nam__e@gmail.com"))
        assertFalse(ValidationUtil.isValidEmail("nam##ef@mail.com"))
        assertFalse(ValidationUtil.isValidEmail(".n@mail.com"))
        assertFalse(ValidationUtil.isValidEmail("n-ame@mail.com"))
        assertFalse(ValidationUtil.isValidEmail("_name@mail.com"))
        assertFalse(ValidationUtil.isValidEmail("+nam#ef@mail.com"))
    }


}