package swimclub.services;

import swimclub.models.*;
import swimclub.models.PaymentStatus;
import swimclub.repositories.MemberRepository;

import java.util.ArrayList;
import java.util.List;

public class PaymentService {


    public double calculateMembershipFee(Member member) {
        if (member.getMembershipStatus() == MembershipStatus.PASSIVE) {
            return 500; // if the member status is passive, the yearly payment will always be 500.
        }

        if (member.getMembershipStatus() == MembershipStatus.ACTIVE && member.getAge() < 18) {
            return 1000; // if the member status is active and the member is beneath 18, the price will be 1000.
        }

        else if (member.getMembershipStatus() == MembershipStatus.ACTIVE && member.getAge() >= 18 && member.getAge() < 60) {
            return 1600; // is the member between 18 and 60 then it's the normal price of 1600.
        }

        else if (member.getMembershipStatus() == MembershipStatus.ACTIVE && member.getAge() >= 60) {
            return 1600 * 0.75; // there will be 25% discount for people above 60 years old.
        }
        return 0;
    }

    public double calculateTotalExpectedPayments (List<Member> memberList) {
        int total = 0;
        for (Member member : memberList) {
            total += calculateMembershipFee(member); //goes through the memberList from the memberRepository.
        }
        return total;
    }

    public List<Member> membersPaidList (List<Member> memberList) {
        List<Member> pendingMembers = new ArrayList<>();

        for (Member member : memberList) {
            if (member.getPaymentStatus() == PaymentStatus.COMPLETE) {
                pendingMembers.add(member);
            }
        }
        return pendingMembers;
    }

    public List<Member> membersPendingList (List<Member> memberList) {
        List<Member> pendingMembers = new ArrayList<>();

        for (Member member : memberList) {
            if (member.getPaymentStatus() == PaymentStatus.PENDING) {
                pendingMembers.add(member);
            }
        }
        return pendingMembers;
    }




}
