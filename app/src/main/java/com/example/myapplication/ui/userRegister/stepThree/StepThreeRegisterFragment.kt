package com.example.myapplication.ui.userRegister.stepThree

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.core.extensionFun.toast
import com.example.myapplication.core.utils.*
import com.example.myapplication.data.datasource.UserRegister
import com.example.myapplication.databinding.FragmentStepThreeRegisterBinding
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StepThreeRegisterFragment : Fragment() {

    private lateinit var mBinding: FragmentStepThreeRegisterBinding
    private val viewModel: StepThreeRegisterViewModel by viewModels()
    private val args: StepThreeRegisterFragmentArgs by navArgs()
    private var model = UserRegister()
    private var imageUriLocal: Uri? = null
    private var isAuthRegister = true

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    imageUriLocal = it.data?.data
                    viewModel.loadPhoto(imageUriLocal.toString())
                }
            }
        }

    /*@Inject
    lateinit var validations: Validations

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String
    private lateinit var lastName: String
    private lateinit var birthdate: String
    private lateinit var phone: String

    //Cast items
    private var position = ""
    private var team = ""
    private var employeeNumber = ""

    private var isValidPosition = false
    private var isValidInitiative = false
    private var isValidEmployeeNumber = false
    private var isValidImage = false
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setModel(args.userModel)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentStepThreeRegisterBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setNameTexView("${args.userModel.name} ${args.userModel.lastName}")
        setObservers()
        setListeners()
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun setObservers() {
        viewModel.statusForDoUser.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    if (it.data!!) {
                        if (isAuthRegister) {
                            isAuthRegister = false
                            viewModel.saveNewUser(createModel())
                        } else
                            viewModel.upLoadImage(imageUriLocal!!)
                    } else {
                        context?.toast("hubo un problema al crear la cuenta")
                    }
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    context?.toast(getString(it.message!!))
                }
            }
        }

        viewModel.statusForDataUser.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    if (it.data!!) {
                        viewModel.upLoadImage(imageUriLocal!!)
                    } else {
                        context?.toast("hubo un problema al crear la cuenta")
                    }
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    context?.toast(getString(it.message!!))
                }
            }
        }
        viewModel.statusForUrl.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (isAdded)
                        (activity as MainActivity).showLoader()
                }
                Status.SUCCESS -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    if (it.data != null) {
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        context?.toast("hubo un problema al crear la cuenta")
                    }
                }
                Status.ERROR -> {
                    if (isAdded)
                        (activity as MainActivity).dismissLoader()
                    context?.toast(getString(it.message!!))
                }
            }
        }


        /*     viewModel.status.observe(viewLifecycleOwner) {
                 when (it.status) {
                     Status.LOADING -> {
                         if (isAdded)
                             (activity as MainActivity).showLoader()
                     }
                      Status.SUCCESS -> {
                         if (isAdded)
                             (activity as MainActivity).dismissLoader()
                         handleStatusSuccess(it)
                     }
                     Status.ERROR -> {
                         if (isAdded)
                             (activity as MainActivity).dismissLoader()
                         context?.toast(getString(it.message!!))
                     }
                 }
             }
     */
        viewModel.setModel.observe(viewLifecycleOwner) {
            model = it
        }

        viewModel.setNameUser.observe(viewLifecycleOwner) {
            mBinding.tvNameUser.text = it
        }

        viewModel.photoUri.observe(viewLifecycleOwner) {
            mBinding.photoUri = viewModel
            validInputs(uri = imageUriLocal)
        }

        viewModel.activeButton.observe(viewLifecycleOwner) {
            activateButton(it)
        }
        /* viewModel.urlPhoto.observe(viewLifecycleOwner) {
             if (it != null) {
                 imageUri = it
                 isValidImage = true
                 updateEnableNextBtn()
             } else
                 context?.toast("Error al subir la imagen")
         }
         viewModel.registerFlag.observe(viewLifecycleOwner) {
             // findNavController().navigate(R.id.action_stepThreeRegisterFragment_to_assistenceMainFragment)
         }*/
    }

    /*private fun handleStatusSuccess(responseStatus: Resource<Boolean>) {
        when(responseStatus.data){
            is Boolean -> {
                if(responseStatus.data){
                    if (isAuthRegister){
                        isAuthRegister = false
                        viewModel.saveNewUser(createModel())
                    } else
                        viewModel.upLoadImage(imageUriLocal!!)
                }else
                    context?.toast("hubo un problema al crear la cuenta")
            }
            else ->{
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
            }
            else -> return
        }
    }*/

    /*private fun setSpinners() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.positions,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mBinding.spinnerPosition.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.teams,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mBinding.spinnerTeam.adapter = adapter
        }
    }*/

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setListeners() {
        /*mBinding.btNext.setOnClickListener {
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

            //sharedPreferences.edit().putString(EMAIL_KEY, email).apply() todo recuperar desde el viewModel
        }
        mBinding.spinnerPosition.onItemSelectedListener = (object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                position = when (pos) {
                    0 -> {
                        "Android"
                    }
                    1 -> {
                        "IOS"
                    }
                    2 -> {
                        "Analyst"
                    }
                    3 -> {
                        "Backend"
                    }
                    4 -> {
                        "Scrum Master"
                    }
                    else -> {
                        "Tester/QA"
                    }
                }
                updateEnableNextBtn()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        })
        mBinding.spinnerTeam.onItemSelectedListener = (object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                team = when (pos) {
                    0 -> {
                        "HellFish"
                    }
                    1 -> {
                        "Minus"
                    }
                    2 -> {
                        "Rocket"
                    }
                    else -> {
                        "En espera"
                    }
                }
                updateEnableNextBtn()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        })
        */

        mBinding.btNext.setOnClickListener {
            viewModel.createAccount(createModel())
            //  requireContext().toast("${mBinding.spinnerPosition.selectedItemPosition}")
        }

        mBinding.spinnerPosition.getPosition {
            validInputs(position = it)
        }

        mBinding.spinnerTeam.getPosition {
            validInputs(team = it)
        }

        mBinding.inputNumEmployee.doAfterTextChanged {
            mBinding.ilNumEmployee.showAndHideError(
                it.toString().isValidCollaboratorNumber(), getString(
                    R.string.label_error_employee
                )
            )
            validInputs(employee = it.toString())
        }
        mBinding.ivChargePhoto.setOnClickListener {
            fromGallery()
        }
    }

    private fun validInputs(
        position: Int = mBinding.spinnerPosition.selectedItemPosition,
        team: Int = mBinding.spinnerTeam.selectedItemPosition,
        employee: String = mBinding.inputNumEmployee.text.toString(),
        uri: Uri? = imageUriLocal
    ) {
        viewModel.validateInputs(
            position,
            team,
            employee,
            uri
        )
    }

    private fun activateButton(status: Boolean) {
        mBinding.btNext.isEnabled = status
    }

    private fun fromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        responseLauncher.launch(intent)
    }

    private fun createModel(): UserRegister {
        return model.apply {
            position = mBinding.spinnerPosition.selectedItemPosition.toString()
            team = mBinding.spinnerTeam.selectedItemPosition.toString()
            profilePhoto = imageUriLocal.toString()
        }
    }

    /*@RequiresApi(Build.VERSION_CODES.M)
    private fun updateEnableNextBtn() {
        //val enable =  isValidPosition && isValidInitiative && isValidEmployeeNumber && isValidImage
        val enable = isValidEmployeeNumber && isValidImage
        val textColor = if (enable) R.color.blue_app else R.color.grey7
        val backgroundColor = if (enable) R.color.white else R.color.grey2

        mBinding.btNext.apply {
            isEnabled = enable
            setBackgroundColor(requireContext().getColor(textColor))
            setTextColor(requireContext().getColor(backgroundColor))
        }
    }*/
}