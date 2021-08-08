package com.tatam.thewheelycoolapp.ui.additems

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.tatam.thewheelycoolapp.R
import com.tatam.thewheelycoolapp.data.model.WheelItem
import com.tatam.thewheelycoolapp.databinding.FragmentAddItemsBinding
import com.tatam.thewheelycoolapp.utils.showToastRes
import com.tatam.thewheelycoolapp.viewmodel.MainViewModel

class AddItemsFragment : Fragment() {

    private lateinit var binding: FragmentAddItemsBinding
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
        setupItemList()
    }

    private fun setupClickListeners(view: View) {
        binding.fabDone.setOnClickListener {
            if (binding.rvItems.size != 0) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_addItemsFragment_to_spinningWheelFragment)
            } else requireActivity().showToastRes(R.string.toast_item_one_item_warning)
        }

        binding.fabAddItem.setOnClickListener { showAddItemDialog() }
    }

    private fun setupItemList() {
        // Using lambdas for click events
        val deleteItem = { wheelItem: WheelItem -> viewModel.deleteItem(wheelItem) }
        val updateItem = { wheelItem: WheelItem -> showUpdateDialog(wheelItem) }

        val adapter = WheelItemAdapter(deleteItem, updateItem)
        binding.rvItems.adapter = adapter

        // Toggling visibility depending if the list is empty or not
        viewModel.getWheelItems.observe(viewLifecycleOwner, {
            binding.fabDone.visibility = if(it.isEmpty()) View.GONE else View.VISIBLE
            binding.tvNoItemsWarning.visibility = if(it.isEmpty()) View.VISIBLE else View.GONE
            adapter.setItems(it)
        })
    }

    private fun showUpdateDialog(wheelItem: WheelItem) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val etName: EditText = view.findViewById(R.id.et_wheel_item_name)
        val btnCancel: Button = view.findViewById(R.id.btn_cancel)
        val btnAdd: Button = view.findViewById(R.id.btn_add_item)

        etName.setText(wheelItem.name)
        btnAdd.text = getString(R.string.update)
        btnAdd.setOnClickListener {
            when {
                inputCheck(etName.text.toString()) -> requireActivity().showToastRes(R.string.toast_item_name_empty_warning)
                databaseCheck(etName.text.toString()) -> requireActivity().showToastRes(R.string.toast_item_name_exists_warning)
                else -> {
                    viewModel.updateItem(WheelItem(wheelItem.id, etName.text.toString()))
                    dialog?.dismiss()
                }
            }
        }

        btnCancel.setOnClickListener { dialog?.dismiss() }

        builder.setView(view)
        dialog = builder.create()
        dialog.show()
    }

    private fun showAddItemDialog() {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_add_item, null)
        val etName: EditText = view.findViewById(R.id.et_wheel_item_name)
        val btnCancel: Button = view.findViewById(R.id.btn_cancel)
        val btnAdd: Button = view.findViewById(R.id.btn_add_item)

        btnAdd.setOnClickListener {
            when {
                inputCheck(etName.text.toString()) -> requireActivity().showToastRes(R.string.toast_item_name_empty_warning)
                databaseCheck(etName.text.toString()) -> requireActivity().showToastRes(R.string.toast_item_name_exists_warning)
                else -> {
                    viewModel.addWheelItem(WheelItem(0, etName.text.toString()))
                    dialog?.dismiss()
                }
            }
        }

        btnCancel.setOnClickListener { dialog?.dismiss() }

        builder.setView(view)
        dialog = builder.create()
        dialog.show()
    }

    private fun databaseCheck(itemName: String): Boolean {
        return viewModel.checkIfItemExists(itemName)
    }

    private fun inputCheck(itemName: String): Boolean {
        return TextUtils.isEmpty(itemName)
    }
}