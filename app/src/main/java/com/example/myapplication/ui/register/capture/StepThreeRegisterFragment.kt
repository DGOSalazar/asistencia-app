package com.example.myapplication.ui.register.capture

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.data.models.User
import com.example.myapplication.databinding.FragmentStepThreeRegisterBinding
import com.example.myapplication.ui.login.EMAIL_KEY
import com.example.myapplication.ui.register.RegisterViewMode
import com.example.myapplication.ui.register.Validations
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepThreeRegisterFragment : Fragment() {

    @Inject
    lateinit var validations:Validations

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var mBinding: FragmentStepThreeRegisterBinding
    private val viewModel : RegisterViewMode by activityViewModels()

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var name:String
    private lateinit var lastName:String
    private lateinit var birthdate:String
    private lateinit var phone:String
    //Cast items
    private var position = ""
    private var team = ""
    private var employeeNumber = ""

    private var isValidPosition = false
    private var isValidInitiative = false
    private var isValidEmployeeNumber = false
    private var isValidImage = false
    var imageUri: Uri? = null
    var foto_gallery: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = arguments?.getString("email")?:""
        password = arguments?.getString("password")?:""
        name = arguments?.getString("name")?:""
        lastName = arguments?.getString("lastName")?:""
        birthdate = arguments?.getString("birthdate")?:""
        phone = arguments?.getString("phone")?:""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding= FragmentStepThreeRegisterBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.tvNameUser.text = "${name} ${lastName}"
        setObservers()
        setSpinners()
        setListeners()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setObservers(){
        viewModel.urlPhoto.observe(viewLifecycleOwner){
            if (it != null) {
                imageUri = it
                isValidImage = true
                updateEnableNextBtn()
            }
             else
                context?.toast("Error al subir la imagen")
        }
        viewModel.registerFlag.observe(viewLifecycleOwner){
            findNavController().navigate(R.id.action_stepThreeRegisterFragment_to_assistenceMainFragment)
        }
    }

    private fun setSpinners() {
         ArrayAdapter.createFromResource(requireContext(), R.array.positions, android.R.layout.simple_spinner_item).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mBinding.spinnerPosition.adapter = adapter
        }
        ArrayAdapter.createFromResource(requireContext(), R.array.teams, android.R.layout.simple_spinner_item).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mBinding.spinnerTeam.adapter = adapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners(){
        mBinding.btNext.setOnClickListener{
                val user = User(
                    email = email,
                    name = name,
                    lastName1 = lastName,
                    lastName2 = lastName,
                    position = position,
                    birthDate = birthdate,
                    team = team,
                    profilePhoto = imageUri.toString(),
                    employee = mBinding.inputPass2.text.toString().toLong(),
                    phone = phone,
                    assistDay = arrayListOf("")
                )
                viewModel.saveUserData(user)

            sharedPreferences.edit().putString(EMAIL_KEY, email).apply()
        }
        mBinding.spinnerPosition.onItemSelectedListener = (object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                position = when(pos){
                    0->{"Android"}
                    1->{"IOS"}
                    2->{"Analyst"}
                    3->{"Backend"}
                    4->{"Scrum Master"}
                    else->{"Tester/QA"}
                }
                updateEnableNextBtn()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        })
        mBinding.spinnerTeam.onItemSelectedListener = (object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                team = when(pos){
                    0->{"HellFish"}
                    1->{"Minus"}
                    2->{"Rocket"}
                    else->{"En espera"}
                }
                updateEnableNextBtn()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        })
        mBinding.inputPass2.doAfterTextChanged {
            employeeNumber = it.toString()
            isValidEmployeeNumber = validations.isValidText(it.toString())
            if (!isValidEmployeeNumber){
                mBinding.ilNumEmployee.isErrorEnabled = true
                mBinding.ilNumEmployee.error = if (isValidEmployeeNumber) null else "Ingrese numero de empleado valido"
            }else
                mBinding.ilNumEmployee.isErrorEnabled = false
            updateEnableNextBtn()
        }
        mBinding.ivChargePhoto.setOnClickListener {
            openGallery()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun updateEnableNextBtn(){
        //val enable =  isValidPosition && isValidInitiative && isValidEmployeeNumber && isValidImage
        val enable = isValidEmployeeNumber && isValidImage
        val textColor = if (enable) R.color.blue_app else R.color.grey7
        val backgroundColor = if (enable) R.color.white else R.color.grey2

        mBinding.btNext.apply {
            isEnabled = enable
            setBackgroundColor(requireContext().getColor(textColor))
            setTextColor(requireContext().getColor(backgroundColor))
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageUri = data?.data
            foto_gallery?.setImageURI(imageUri)
            mBinding.ivChargePhoto.setImageURI(imageUri)
            imageUri?.let { viewModel.uploadImage(it) }
        }
    }

}