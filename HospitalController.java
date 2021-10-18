package com.main.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.main.model.AddPhysicianPojo;
import com.main.model.EnrollPatientPojo;
import com.main.model.PatientDiagnosisDetails;
import com.main.service.HospitalServiceImpl;

@Controller
public class HospitalController {
	@Autowired
	HospitalServiceImpl regSvrImpl;

	@RequestMapping("/home")
	public ModelAndView homePage() {

		return new ModelAndView("Home");
	}
	
	@RequestMapping("/loginPage")
	public ModelAndView loginPage() {

		return new ModelAndView("LoginPage");
	}
	
	@RequestMapping("/loginSucc")
	public ModelAndView loginSucc() {

		return new ModelAndView("EnrollPatient");
	}
	

	@RequestMapping("/registerPatient")
	public ModelAndView loadRegister(@ModelAttribute("patientReg") EnrollPatientPojo patientReg) {

		return new ModelAndView("EnrollPatient");
	}

	@RequestMapping("/patientRegSucess")
	public ModelAndView nextPage(@Validated @ModelAttribute("patientReg") EnrollPatientPojo patientReg,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ModelAndView("Register");
		}
		regSvrImpl.savePatientRegDetails(patientReg);
		return new ModelAndView("redirect:/fetchPatient");
	}

	@RequestMapping("/fetchPatient")
	public ModelAndView fetchPatientDetails() {
		List<EnrollPatientPojo> list = regSvrImpl.getPatientDet();
		return new ModelAndView("MyPatientData", "patientDetails", list);

	}
	@RequestMapping("/deletePatient/{id}")
	public ModelAndView deletePatientData(@PathVariable("id") int id) {
		regSvrImpl.deletePatient(id);
		return new ModelAndView("redirect:/fetchPatient");
	}
	
	

	@RequestMapping("/addPhysician")
	public ModelAndView loadPhysician() {

		return new ModelAndView("AddPhysician");
	}

	@RequestMapping("/addPhysicianSucess")
	public ModelAndView addPhysicianSucess(@Validated @ModelAttribute("addPhysician") AddPhysicianPojo addPhysician,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ModelAndView("AddPhysician");
		}
		regSvrImpl.savePhysicianDetails(addPhysician);
		return new ModelAndView("redirect:/fetchMyPhy");
	}

	@RequestMapping("/fetchMyPhy")
	public ModelAndView fetchLoginDetails() {
		List<AddPhysicianPojo> list = regSvrImpl.getMyPhysician();
		return new ModelAndView("MyPhysicianData", "phyDetails", list);
	}

	@RequestMapping("/searchPhysicianData")
	public ModelAndView searchBasedOnFields(@RequestParam("state") String state,
			@RequestParam("department") String department, @RequestParam("insurancePlan") String insurancePlan,
			Model model) {
		List<AddPhysicianPojo> listData = regSvrImpl.getSearchBasedOnFields(state, department, insurancePlan);
		return new ModelAndView("SearchMyPhysician", "searchPhy", listData);
	}

	@RequestMapping("/deletePhy/{id}")
	public ModelAndView deleteUserData(@PathVariable("id") int id) {
		regSvrImpl.deletePhy(id);
		return new ModelAndView("redirect:/fetchMyPhy");
	}

	@RequestMapping("/addDiagnosisDetails")
	public ModelAndView addDiagnosisDetails() {
		List<EnrollPatientPojo> listDiag = regSvrImpl.getPatientDet();
		List<AddPhysicianPojo> list = regSvrImpl.getMyPhysician();
		return new ModelAndView("PatientDiagnosisDetails", "DiagnosisLists", listDiag);
	}
	@RequestMapping("/patientDiagnosisSucess")
	public ModelAndView nextPage(@Validated @ModelAttribute("diagnosisDet") PatientDiagnosisDetails diagnosisDet, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("Register");
		}
		regSvrImpl.saveDiagnosisData(diagnosisDet);
		List<EnrollPatientPojo> listDiag = regSvrImpl.getPatientDet();
		List<AddPhysicianPojo> list = regSvrImpl.getMyPhysician();
		return new ModelAndView("PatientDiagnosisDetails", "DiagnosisLists", listDiag);
	}

}
