format 224

classcanvas 128002 class_ref 128514 // Server
  classdiagramsettings member_max_width 0 end
  xyz 17.6 192.7 2000
end
classcanvas 128130 class_ref 128386 // ConnectionHandler
  classdiagramsettings member_max_width 0 end
  xyz 461.1 201.1 3005
end
classcanvas 128898 class_ref 128002 // Client
  classdiagramsettings member_max_width 0 end
  xyz 144.7 18.3 2005
end
classcanvas 129026 class_ref 128258 // InputHandler
  classdiagramsettings member_max_width 0 end
  xyz 546.2 44.7 3012
end
relationcanvas 128258 relation_ref 128386 // <unidirectional association>
  from ref 128130 z 3006 to ref 128002
  role_a_pos 340 267 3000 no_role_b
  no_multiplicity_a no_multiplicity_b
end
relationcanvas 128386 relation_ref 128514 // <unidirectional association>
  decenter_begin 488
  decenter_end 694
  from ref 128130 z 3006 stereotype "<<List>>" xyz 612 182.5 3006 to point 633.9 163.3
  line 128514 z 3006 to point 706.8 163.4
  line 128642 z 3006 to ref 128130
  role_a_pos 714 188 3000 no_role_b
  multiplicity_a_pos 695 188 3000 no_multiplicity_b
end
relationcanvas 128770 relation_ref 129026 // <unidirectional association>
  from ref 128002 z 3006 stereotype "<<ArrayList>>" xyz 361 285.5 3006 to ref 128130
  role_a_pos 391 267 3000 no_role_b
  no_multiplicity_a no_multiplicity_b
end
relationcanvas 129154 relation_ref 128770 // <unidirectional association>
  from ref 129026 z 3013 to ref 128898
  role_a_pos 335 69 3000 no_role_b
  no_multiplicity_a no_multiplicity_b
end
relationcanvas 129282 relation_ref 128130 // <unidirectional association>
  from ref 128898 z 3013 to ref 129026
  role_a_pos 473 70 3000 no_role_b
  no_multiplicity_a no_multiplicity_b
end
end
