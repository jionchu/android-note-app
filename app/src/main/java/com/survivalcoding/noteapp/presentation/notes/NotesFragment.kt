package com.survivalcoding.noteapp.presentation.notes

import AddEditFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.survivalcoding.noteapp.App
import com.survivalcoding.noteapp.R
import com.survivalcoding.noteapp.databinding.FragmentNotesBinding
import com.survivalcoding.noteapp.domain.model.Note
import com.survivalcoding.noteapp.domain.model.SortFactor
import com.survivalcoding.noteapp.domain.model.SortType
import com.survivalcoding.noteapp.presentation.notes.adapter.NoteListAdapter

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<NotesViewModel> {
        NotesViewModelFactory((requireActivity().application as App).repository)
    }
    private val snackbar by lazy {
        Snackbar.make(
            binding.root,
            R.string.deleted_message, Snackbar.LENGTH_SHORT
        ).setAction(R.string.delete_undo) { viewModel.undoDelete() }
    }
    private val adapter by lazy {
        NoteListAdapter({ note ->
            viewModel.deleteNote(note)
            snackbar.show()
        }, { note ->
            moveToAdd(note)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // recyclerView 설정
        binding.notesRecyclerView.adapter = adapter

        // 작성하기 화면으로 이동
        binding.notesFabAdd.setOnClickListener { moveToAdd(null) }

        // note list 업데이트 관찰
        viewModel.notes.observe(this) { list ->
            adapter.submitList(list)
        }

        // 정렬 옵션 보이기
        binding.notesIvSort.setOnClickListener {
            binding.notesLlSort.visibility =
                if (binding.notesLlSort.visibility == VISIBLE) GONE else VISIBLE
        }

        // 정렬 옵션 변경
        binding.notesRbTitle.setOnClickListener { viewModel.setSortFactor(SortFactor.TITLE) }
        binding.notesRbDate.setOnClickListener { viewModel.setSortFactor(SortFactor.TIMESTAMP) }
        binding.notesRbColor.setOnClickListener { viewModel.setSortFactor(SortFactor.COLOR) }
        binding.notesRbAsc.setOnClickListener { viewModel.setSortType(SortType.ASC) }
        binding.notesRbDesc.setOnClickListener { viewModel.setSortType(SortType.DESC) }

        // 정렬 옵션 변경 시 리스트 조회
        viewModel.sortFactor.observe(this) { viewModel.getNotes() }
        viewModel.sortType.observe(this) { viewModel.getNotes() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun moveToAdd(note: Note?) {
        parentFragmentManager.commit {
            replace<AddEditFragment>(
                R.id.main_fragment_container_view,
                args = bundleOf("noteId" to note?.id),
            )
            setReorderingAllowed(true)
            addToBackStack(null) // name can be null
        }
    }
}