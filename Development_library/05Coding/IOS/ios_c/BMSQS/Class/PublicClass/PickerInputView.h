//
//  ShootStatusInputTableViewCell.h
//  ShootStudio
//
//  Created by Tom Fewster on 18/10/2011.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>

@class PickerInputView;
@protocol PickerInputViewDelegate <NSObject>

-(void)picker:(PickerInputView*)picker didClickDoneButton:(id)button;
-(void)didClickDoneButton;

@end

@interface PickerInputView : UIView <UIKeyInput, UIPopoverControllerDelegate> {
    // For iPad
    UIPopoverController *popoverController;
    UIToolbar *inputAccessoryView;
}
@property(nonatomic,strong) UIImageView *backgroundImageView;
@property(nonatomic,strong) UIPickerView *picker;
@property(nonatomic,strong) UILabel* numberLabel;
@property(nonatomic,strong) UILabel* dateLabel;
@property(nonatomic,strong) UILabel* headLabel;
@property(nonatomic,weak)   id<PickerInputViewDelegate> delegate;
@end
